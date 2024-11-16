package dgu.se.bananavote.vote_info_service.Policy;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Iterator;

@Service
public class PolicyUpdater {

    private final PolicyService policyService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    @Autowired
    public PolicyUpdater(PolicyService policyService,
                            RestTemplate restTemplate,
                            ObjectMapper objectMapper) {
        this.policyService = policyService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }
    // 서비스키값, URL 설정
    @Value("${api.serviceKey}")
    private String serviceKey;
    private final String API_URL_TEMPLATE = "http://apis.data.go.kr/9760000/PartyPlcInfoInqireService/getPartyPlcInfoInqire"+
            "?resultType=json&serviceKey=%s&pageNo=%d&numOfRows=100&sgId=20220309";

    public void updatePolicies() {
        int pageNo = 1;
        boolean hasNextPage = true;

        while (hasNextPage) {
            try {
                // URL 생성
                String apiUrl = String.format(API_URL_TEMPLATE, serviceKey, pageNo);

                // URI로 변환
                URI uri = new URI(apiUrl);

                // API 호출
                String response = restTemplate.getForObject(uri, String.class);

                // JSON 데이터 파싱
                JsonNode rootNode = objectMapper.readTree(response);
                JsonNode bodyNode = rootNode.path("response").path("body").path("items").path("item");

                if (bodyNode.isMissingNode() || !bodyNode.isArray() || bodyNode.size() == 0) {
                    hasNextPage = false;
                    break;
                }

                // 데이터를 반복 처리하여 저장
                Iterator<JsonNode> elements = bodyNode.elements();
                while (elements.hasNext()) {
                    JsonNode itemNode = elements.next();

                    // 필요한 데이터 추출
                    String jdName = itemNode.path("jdName").asText();
                    int prmsOrder = itemNode.path("prmsOrder").asInt();

                    // Policy 객체 생성
                    Policy policy = new Policy();
                    policy.setJdName(jdName);
                    policy.setPrmsOrder(prmsOrder);

                    // PolicyService를 통해 저장
                    policyService.savePolicy(policy);
                }

                // 다음 페이지로 이동
                pageNo++;

            } catch (Exception e) {
                // 예외 처리 (로그 출력 등)
                e.printStackTrace();
                hasNextPage = false;
            }
        }
    }



}
