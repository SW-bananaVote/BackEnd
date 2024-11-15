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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CandidateDataUpdater {

    private final CandidateRepository candidateRepository;
    private final CareerRepository careerRepository;
    private final DistrictService districtService;
    private final PartyService partyService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.serviceKey}")
    private String serviceKey;

    private final String API_URL_TEMPLATE = "https://apis.data.go.kr/9760000/PofelcddInfoInqireService/getPofelcddRegistSttusInfoInqire" +
            "?resultType=json&serviceKey=%s&pageNo=%d&numOfRows=100&sgId=20240410&sgTypecode=2&sggName=%s&sdName=%s&jdName=%s";

    @Autowired
    public CandidateDataUpdater(CandidateRepository candidateRepository,
                                CareerRepository careerRepository,
                                DistrictService districtService,
                                PartyService partyService,
                                RestTemplate restTemplate,
                                ObjectMapper objectMapper) {
        this.candidateRepository = candidateRepository;
        this.careerRepository = careerRepository;
        this.districtService = districtService;
        this.partyService = partyService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public void updateCandidateData() {
        List<District> districts = districtService.getDistrict();
        List<Party> parties = partyService.getAllParties();

        for (District district : districts) {

            // 한글 데이터 URL 인코딩
            String encodedSggName = URLEncoder.encode(district.getSggName(), StandardCharsets.UTF_8);
            String encodedSdName = URLEncoder.encode(district.getSdName(), StandardCharsets.UTF_8);

//            for (Party party : parties) {

                String encodedJdName = URLEncoder.encode("더불어민주당", StandardCharsets.UTF_8);

                int pageNo = 1;
                int totalPages = 1;

                try {
                    while (pageNo <= totalPages) {
                        // URL 구성 및 URI로 변환
                        String apiUrl = String.format(API_URL_TEMPLATE, serviceKey, pageNo, encodedSggName, encodedSdName, encodedJdName);
                        URI uri = new URI(apiUrl);
                        System.out.println(uri);

                        // API 호출
                        String jsonResponse = restTemplate.getForObject(uri, String.class);
                        System.out.println("API Response: " + jsonResponse);

                        // JSON 응답 파싱
                        JsonNode root = objectMapper.readTree(jsonResponse);
                        JsonNode header = root.path("response").path("header");
                        String resultCode = header.path("resultCode").asText();
                        String resultMsg = header.path("resultMsg").asText();

                        // 응답 결과 확인
                        if ("INFO-03".equals(resultCode)) {
                            break;
                        }

                        if (!"INFO-00".equals(resultCode)) {
                            System.err.println("Unexpected response code: " + resultCode + " - " + resultMsg);
                            break;
                        }

                        JsonNode body = root.path("response").path("body");
                        JsonNode items = body.path("items").path("item");

                        if (pageNo == 1) {
                            int totalCount = body.path("totalCount").asInt();
                            int numOfRows = body.path("numOfRows").asInt();
                            totalPages = (int) Math.ceil((double) totalCount / numOfRows);
                        }

                        // 후보자 데이터 처리
                        for (JsonNode item : items) {

                            Candidate candidate = new Candidate();
                            candidate.setCnddtId(item.path("huboid").asText());
                            candidate.setSgId(item.path("sgId").asText());
                            candidate.setJdName(item.path("jdName").asText());
                            candidate.setWiwName(item.path("wiwName").asText());
                            candidate.setName(item.path("name").asText());

                            candidateRepository.save(candidate);

                            // 경력 데이터 처리
                            int careerOrder = 1;
                            while (item.has("career" + careerOrder)) {
                                String careerDetail = item.path("career" + careerOrder).asText();
                                if (!careerDetail.isEmpty()) {
                                    Career career = new Career();
                                    career.setCnddtId(candidate.getCnddtId());
                                    career.setCareerOrder(careerOrder);
                                    career.setCareer(careerDetail);

                                    careerRepository.save(career);
                                }
                                careerOrder++;
                            }
                        }

                        pageNo++;
                    }
                } catch (URISyntaxException e) {
                    System.err.println("URI Syntax Error: " + e.getMessage());
                } catch (HttpClientErrorException e) {
                    System.err.println("HTTP Client Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
                } catch (HttpServerErrorException e) {
                    System.err.println("HTTP Server Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
                } catch (Exception e) {
                    System.err.println("Unexpected Error: " + e.getMessage());
                    e.printStackTrace();
                }
//            }
        }
    }
}
