package dgu.se.bananavote.vote_info_service.Candidate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dgu.se.bananavote.vote_info_service.District.District;
import dgu.se.bananavote.vote_info_service.District.DistrictService;
import dgu.se.bananavote.vote_info_service.Party.Party;
import dgu.se.bananavote.vote_info_service.Party.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class CandidateDataUpdater {

    private final CandidateService candidateService;
    private final DistrictService districtService;
    private final PartyService partyService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.serviceKey}")
    private String serviceKey;

    private final String API_URL_TEMPLATE = "https://apis.data.go.kr/9760000/PofelcddInfoInqireService/getPofelcddRegistSttusInfoInqire" +
            "?serviceKey=%s&pageNo=%d&numOfRows=100&sgId=%s&sgTypecode=%s&sggName=%s&sdName=%s&jdName=%s";

    @Autowired
    public CandidateDataUpdater(CandidateService candidateService,
                                DistrictService districtService,
                                PartyService partyService,
                                RestTemplate restTemplate,
                                ObjectMapper objectMapper) {
        this.candidateService = candidateService;
        this.districtService = districtService;
        this.partyService = partyService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public void updateCandidateData() {
        List<District> districts = districtService.getDistrict(); // 모든 District 가져오기
        List<Party> parties = partyService.getAllParties(); // 모든 Party 가져오기

        for (District district : districts) {
            String sggName = district.getSggName();
            String sdName = district.getSdName();

            for (Party party : parties) {
                String jdName = party.getPartyName(); // 각 정당 이름 사용

                int pageNo = 1;
                int totalPages = 1;

                try {
                    while (pageNo <= totalPages) {
                        // API URL 구성
                        String apiUrl = String.format(API_URL_TEMPLATE, serviceKey, pageNo, "20240410", "2", sggName, sdName, jdName);
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

                        // 후보자 데이터 저장
                        for (JsonNode item : items) {
                            Candidate candidate = new Candidate();
                            candidate.setCnddtId(item.path("huboid").asText());
                            candidate.setSgId(item.path("sgId").asText());
                            candidate.setJdName(item.path("jdName").asText());
                            candidate.setWiwName(item.path("wiwName").asText());
                            candidate.setName(item.path("name").asText());

                            candidateService.saveCandidate(candidate); // 데이터베이스에 저장
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
    }
}
