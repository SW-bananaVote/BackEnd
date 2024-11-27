package dgu.se.bananavote.vote_info_service.policy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dgu.se.bananavote.vote_info_service.party.Party;
import dgu.se.bananavote.vote_info_service.party.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class PolicyDataUpdater {

    private final PartyService partyService;
    private final PolicyService policyService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.serviceKey}")
    private String serviceKey;

    private final String API_URL_TEMPLATE = "http://apis.data.go.kr/9760000/PartyPlcInfoInqireService/getPartyPlcInfoInqire" +
            "?resultType=json&serviceKey=%s&pageNo=%d&numOfRows=100&sgId=20240410&sgTypecode=2&partyName=%s";

    @Autowired
    public PolicyDataUpdater(PartyService partyService, PolicyService policyService, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.partyService = partyService;
        this.policyService = policyService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public void updatePolicyData() {
        // 정당 목록
        List<Party> parties = partyService.getAllParties();

        for (Party party : parties) {
            String partyName = party.getPartyName();
            fetchAndSavePoliciesForParty(partyName);
        }
    }

    private void fetchAndSavePoliciesForParty(String partyName) {
        int pageNo = 1;
        boolean hasNextPage = true;
        String encodedPartyName = URLEncoder.encode(partyName, StandardCharsets.UTF_8);

        while (hasNextPage) {
            try {
                // URL 생성
                String apiUrl = String.format(API_URL_TEMPLATE, serviceKey, pageNo, encodedPartyName);

                // URI 생성 및 API 호출
                URI uri = new URI(apiUrl);
                String response = restTemplate.getForObject(uri, String.class);
                System.out.println(uri);
                System.out.println(response);

                // JSON 데이터 파싱
                JsonNode rootNode = objectMapper.readTree(response);
                JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

                if (itemsNode.isMissingNode() || !itemsNode.isArray()) {
                    System.out.println("No more data available for party: " + partyName);
                    hasNextPage = false;
                    break;
                }

                // 데이터 처리
                for (JsonNode itemNode : itemsNode) {
                    int prmsCnt = itemNode.path("prmsCnt").asInt();

                    for (int i = 1; i <= prmsCnt; i++) {
                        int prmsOrder = itemNode.path("prmsOrd" + i).asInt(0);
                        String prmsTitle = itemNode.path("prmsTitle" + i).asText("");
                        String prmsCont = itemNode.path("prmmCont" + i).asText("");

                        if (prmsOrder == 0 || prmsTitle.isEmpty() || prmsCont.isEmpty()) {
                            continue; // 데이터가 없으면 건너뜀
                        }

                        // Policy 객체 생성 및 저장
                        // 중복 확인
                        if (!policyService.existsByPartyNameAndPrmsOrder(partyName, prmsOrder)) {
                            // Policy 객체 생성 및 저장
                            Policy policy = new Policy();
                            policy.setJdName(partyName);
                            policy.setPrmsOrder(prmsOrder);
                            policy.setPrmsTitle(prmsTitle);
                            policy.setPrmsCont(prmsCont);

                            policyService.savePolicy(policy);
                            System.out.println("Saved Policy for " + partyName + ": " + policy.getPrmsTitle());
                        } else {
                            System.out.println("Duplicate policy detected for " + partyName + " with order " + prmsOrder);
                        }
                    }
                }

                // 페이징 처리
                int totalCount = rootNode.path("response").path("body").path("totalCount").asInt();
                int numOfRows = rootNode.path("response").path("body").path("numOfRows").asInt();
                hasNextPage = pageNo * numOfRows < totalCount;
                pageNo++;

            } catch (URISyntaxException e) {
                System.err.println("URI Syntax Error for party " + partyName + ": " + e.getMessage());
                hasNextPage = false;
            } catch (Exception e) {
                System.err.println("Error while updating policies for party " + partyName + ": " + e.getMessage());
                hasNextPage = false;
            }
        }
    }
}


