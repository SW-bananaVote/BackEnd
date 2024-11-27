package dgu.se.bananavote.vote_info_service.poll;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dgu.se.bananavote.vote_info_service.district.District;
import dgu.se.bananavote.vote_info_service.district.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class PollDataUpdater {

    private final PollService pollService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final DistrictService districtService;

    @Value("${api.serviceKey}")
    private String serviceKey;

    private final String API_URL_TEMPLATE =
            "https://apis.data.go.kr/9760000/PolplcInfoInqireService2/getPolplcOtlnmapTrnsportInfoInqire"
                    + "?resultType=json&serviceKey=%s&pageNo=%d&numOfRows=100&sgId=20240410&sdName=%s&wiwName=%s";

    @Autowired
    public PollDataUpdater(PollService pollService, RestTemplate restTemplate, ObjectMapper objectMapper, DistrictService districtService) {
        this.pollService = pollService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.districtService = districtService;
    }

    @Scheduled(cron = "0 0 1 * * *")
    @Transactional
    public void updatePollData() {
        List<District> districts = districtService.getDistrict(); // 모든 District 가져오기

        for (District district : districts) {
            updatePollDataForDistrict(district); // 각 District에 대해 투표소 정보 업데이트
        }
    }

    private void updatePollDataForDistrict(District district) {
        int pageNo = 1;
        int totalPages = 1;

//        List<Poll> allPolls = new ArrayList<>();

        try {
            while (pageNo <= totalPages) {
                // District의 sdName, sggName을 인코딩
                String encodedSdName = URLEncoder.encode(district.getSdName(), StandardCharsets.UTF_8);
                String encodedSggName = URLEncoder.encode(district.getSggName(), StandardCharsets.UTF_8);

                // API URL 생성
                String apiUrl = String.format(API_URL_TEMPLATE, serviceKey, pageNo, encodedSdName, encodedSggName);
                URI uri = new URI(apiUrl);

                // API 호출
                String jsonResponse = restTemplate.getForObject(uri, String.class);
                if (jsonResponse == null || jsonResponse.isEmpty()) {
                    System.err.println("Empty or null response from API for district: " + district.getSdName() + ", " + district.getSggName());
                    break;
                }

                try {
                    JsonNode root = objectMapper.readTree(jsonResponse);

                    // 예상치 못한 JSON 구조 방어
                    JsonNode response = root.path("response");
                    if (response.isMissingNode()) {
                        throw new IllegalArgumentException("Missing 'response' node in API response for district: " + district.getSdName());
                    }

                    JsonNode body = response.path("body");
                    if (body.isMissingNode()) {
                        throw new IllegalArgumentException("Missing 'body' node in API response for district: " + district.getSdName());
                    }

                    JsonNode items = body.path("items").path("item");
                    if (!items.isArray()) {
                        throw new IllegalArgumentException("Expected 'item' to be an array in API response for district: " + district.getSdName());
                    }

                    if (pageNo == 1) {
                        int totalCount = body.path("totalCount").asInt(0); // 기본값 0
                        int numOfRows = body.path("numOfRows").asInt(1); // 기본값 1
                        totalPages = (int) Math.ceil((double) totalCount / numOfRows);
                    }

                    for (JsonNode item : items) {
                        String sgId = item.path("sgId").asText("");
                        String psName = item.path("psName").asText("");
                        String addr = item.path("addr").asText("");

                        // 중복 확인
                        if (!pollService.existsBySgIdAndPsNameAndAddr(sgId, psName, addr)) {
                            Poll poll = new Poll();
                            poll.setSgId(sgId);
                            poll.setPsName(psName);
                            poll.setSdName(item.path("sdName").asText(""));
                            poll.setWiwName(item.path("wiwName").asText(""));
                            poll.setEmdName(item.path("emdName").asText(""));
                            poll.setPlaceName(item.path("placeName").asText(""));
                            poll.setAddr(addr);
                            poll.setFloor(item.path("floor").asText(""));
                            pollService.savePoll(poll); // Poll 데이터 저장
                            System.out.println("Saved Poll: " + psName);
                        } else {
                            System.out.println("Duplicate Poll detected: " + psName);
                        }
                    }
                } catch (Exception parseException) {
                    System.err.println("Error parsing JSON response for district: " + district.getSdName() + ", " + district.getSggName());
                    parseException.printStackTrace();
                    break; // JSON 파싱 오류 시 중단
                }

                pageNo++;
            }
        } catch (Exception e) {
            System.err.println("Error updating poll data for district: " + district.getSdName() + ", " + district.getSggName());
            e.printStackTrace();
        }
    }

}
