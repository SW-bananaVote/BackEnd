package dgu.se.bananavote.vote_info_service.party;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class PartyDataUpdater {

    private final PartyService partyService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.serviceKey}")
    private String serviceKey;

    private final String API_URL_TEMPLATE = "https://apis.data.go.kr/9760000/CommonCodeService/getCommonPartyCodeList" +
            "?resultType=json&serviceKey=%s&pageNo=%d&numOfRows=100&sgId=20240410";

    @Autowired
    public PartyDataUpdater(PartyService partyService,
                            RestTemplate restTemplate,
                            ObjectMapper objectMapper) {
        this.partyService = partyService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Scheduled(cron = "0 0 1 * * *")
    @Transactional
    public void updatePartyData() {
        int pageNo = 1;
        int totalPages = 1;

        try {
            while (pageNo <= totalPages) {
                // API URL 구성
                String apiUrl = String.format(API_URL_TEMPLATE, serviceKey, pageNo);
                URI uri = new URI(apiUrl);

                // API 호출
                String jsonResponse = restTemplate.getForObject(uri, String.class);

                // JSON 파싱
                JsonNode root = objectMapper.readTree(jsonResponse);
                JsonNode body = root.path("response").path("body");
                JsonNode items = body.path("items").path("item");

                if (pageNo == 1) {
                    int totalCount = body.path("totalCount").asInt();
                    int numOfRows = body.path("numOfRows").asInt();
                    totalPages = (int) Math.ceil((double) totalCount / numOfRows);
                }

                // 정당 데이터 저장
                for (JsonNode item : items) {
                    String partyId = item.path("pOrder").asText();
                    String partyName = item.path("jdName").asText();

                    // 중복 확인
                    if (!partyService.existsByPartyId(partyId)) {
                        Party party = new Party();
                        party.setPartyId(partyId);
                        party.setPartyName(partyName);

                        partyService.saveParty(party); // 데이터베이스에 저장
                    }
                }

                pageNo++;
            }
        } catch (URISyntaxException e) {
            System.err.println("URI Syntax Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
