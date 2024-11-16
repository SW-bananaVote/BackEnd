package dgu.se.bananavote.vote_info_service.Policy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class PolicyUpdater {

    private final PolicyService policyService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.serviceKey}")
    private String serviceKey;

    private final String API_URL_TEMPLATE = "http://apis.data.go.kr/9760000/PartyPlcInfoInqireService/getPartyPlcInfoInqire" +
            "?resultType=json&serviceKey=%s&pageNo=%d&numOfRows=10&sgId=20220309&partyName=%s";
    String encodedJdName = URLEncoder.encode("더불어민주당", StandardCharsets.UTF_8);

    @Autowired
    public PolicyUpdater(PolicyService policyService, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.policyService = policyService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public void updatePolicies() {
        int pageNo = 1;
        boolean hasNextPage = true;

        while (hasNextPage) {
            try {
                // URL 생성
                String apiUrl = String.format(API_URL_TEMPLATE, serviceKey, pageNo, encodedJdName);

                // URI 생성 및 API 호출
                URI uri = new URI(apiUrl);
                String response = restTemplate.getForObject(uri, String.class);

                // JSON 데이터 파싱
                JsonNode rootNode = objectMapper.readTree(response);
                JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

                if (itemsNode.isMissingNode() || !itemsNode.isArray()) {
                    System.out.println("No more data available");
                    hasNextPage = false;
                    break;
                }

                // 데이터 처리
                for (JsonNode itemNode : itemsNode) {
                    String partyName = itemNode.path("partyName").asText();

                    int prmsCnt = itemNode.path("prmsCnt").asInt();

                    // 각 정책 데이터 추출
                    for (int i = 1; i <= prmsCnt; i++) {
                        int prmsOrder = itemNode.path("prmsOrd" + i).asInt(0);
                        String prmsTitle = itemNode.path("prmsTitle" + i).asText("");
                        String prmsCont = itemNode.path("prmmCont" + i).asText("");

                        if (prmsOrder == 0 || prmsTitle.isEmpty() || prmsCont.isEmpty()) {
                            continue; // 데이터가 없으면 건너뜀
                        }

                        // Policy 객체 생성 및 저장
                        Policy policy = new Policy();
                        policy.setJdName(partyName);
                        policy.setPrmsOrder(prmsOrder);
                        policy.setPrmsTitle(prmsTitle);
                        policy.setPrmsCont(prmsCont);

                        policyService.savePolicy(policy);
                        System.out.println("Saved Policy: " + policy.getPrmsCont());
                    }
                }

                // 페이징 처리
                int totalCount = rootNode.path("response").path("body").path("totalCount").asInt();
                int numOfRows = rootNode.path("response").path("body").path("numOfRows").asInt();
                hasNextPage = pageNo * numOfRows < totalCount;
                pageNo++;

            } catch (URISyntaxException e) {
                System.err.println("URI Syntax Error: " + e.getMessage());
                hasNextPage = false;
            } catch (Exception e) {
                System.err.println("Error while updating policies: " + e.getMessage());
                hasNextPage = false;
            }
        }
    }
}

