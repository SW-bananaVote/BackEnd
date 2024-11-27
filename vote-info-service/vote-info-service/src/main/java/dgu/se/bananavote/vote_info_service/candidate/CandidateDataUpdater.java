package dgu.se.bananavote.vote_info_service.candidate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dgu.se.bananavote.vote_info_service.district.District;
import dgu.se.bananavote.vote_info_service.district.DistrictService;
import dgu.se.bananavote.vote_info_service.party.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CandidateDataUpdater {

    private final CandidateService candidateService;
    private final CareerService careerService;
    private final DistrictService districtService;
    private final PartyService partyService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.serviceKey}")
    private String serviceKey;

    private final String API_URL_TEMPLATE = "https://apis.data.go.kr/9760000/PofelcddInfoInqireService/getPofelcddRegistSttusInfoInqire" +
            "?resultType=json&serviceKey=%s&pageNo=%d&numOfRows=100&sgId=20240410&sgTypecode=2&sggName=%s&sdName=%s&jdName=%s";

    @Autowired
    public CandidateDataUpdater(CandidateService candidateService,
                                CareerService careerService,
                                DistrictService districtService,
                                PartyService partyService,
                                RestTemplate restTemplate,
                                ObjectMapper objectMapper) {
        this.candidateService = candidateService;
        this.careerService = careerService;
        this.districtService = districtService;
        this.partyService = partyService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Scheduled(cron = "0 0 1 * * *")
    @Transactional
    public void updateCandidateData() {
        List<District> districts = districtService.getDistrict();
        if (districts.isEmpty()) {
            System.err.println("No districts found. Skipping candidate data update.");
            return;
        }

        List<String> parties = List.of("더불어민주당", "국민의힘", "녹색정의당");

        for (District district : districts) {
            String encodedSggName = URLEncoder.encode(district.getSggName(), StandardCharsets.UTF_8);
            String encodedSdName = URLEncoder.encode(district.getSdName(), StandardCharsets.UTF_8);

            for (String party : parties) {
                processPartyCandidates(encodedSggName, encodedSdName, party);
            }
        }
    }

    private void processPartyCandidates(String encodedSggName, String encodedSdName, String party) {
        String encodedJdName = URLEncoder.encode(party, StandardCharsets.UTF_8);

        int pageNo = 1;
        int totalPages = 1;

        try {
            while (pageNo <= totalPages) {
                String apiUrl = String.format(API_URL_TEMPLATE, serviceKey, pageNo, encodedSggName, encodedSdName, encodedJdName);
                URI uri = new URI(apiUrl);
                System.out.println("Requesting API: " + uri);

                String jsonResponse = restTemplate.getForObject(uri, String.class);
                if (jsonResponse == null || jsonResponse.isEmpty()) {
                    System.err.println("Empty response from API.");
                    break;
                }

                totalPages = processApiResponse(jsonResponse, pageNo, totalPages);
                pageNo++;
            }
        } catch (URISyntaxException e) {
            System.err.println("URI Syntax Error: " + e.getMessage());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("HTTP Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int processApiResponse(String jsonResponse, int pageNo, int totalPages) throws Exception {
        JsonNode root = objectMapper.readTree(jsonResponse);

        // Validate response headers
        JsonNode header = root.path("response").path("header");
        String resultCode = header.path("resultCode").asText();
        String resultMsg = header.path("resultMsg").asText();

        if ("INFO-03".equals(resultCode)) {
            System.out.println("No more data available for this request.");
            return totalPages;
        }

        if (!"INFO-00".equals(resultCode)) {
            System.err.println("Unexpected response code: " + resultCode + " - " + resultMsg);
            return totalPages;
        }

        // Process response body
        JsonNode body = root.path("response").path("body");
        JsonNode items = body.path("items").path("item");

        if (pageNo == 1) {
            int totalCount = body.path("totalCount").asInt();
            int numOfRows = body.path("numOfRows").asInt();
            totalPages = (int) Math.ceil((double) totalCount / numOfRows);
        }

        items.forEach(this::processCandidate);
        return totalPages;
    }

    private void processCandidate(JsonNode item) {
        String cnddtId = item.path("huboid").asText();

        if (!candidateService.existsByCnddtId(cnddtId)) {
            Candidate candidate = new Candidate();
            candidate.setCnddtId(cnddtId);
            candidate.setSgId(item.path("sgId").asText());
            candidate.setJdName(item.path("jdName").asText());
            candidate.setWiwName(item.path("wiwName").asText());
            candidate.setName(item.path("name").asText());

            candidateService.saveCandidate(candidate);
            System.out.println("Saved Candidate: " + candidate.getName());
        } else {
            System.out.println("Duplicate Candidate detected: " + cnddtId);
        }

        processCandidateCareers(item, cnddtId);
    }

    private void processCandidateCareers(JsonNode item, String cnddtId) {
        int careerOrder = 1;

        while (item.has("career" + careerOrder)) {
            String careerDetail = item.path("career" + careerOrder).asText();

            if (!careerDetail.isEmpty() && !careerService.existsByCnddtIdAndCareerOrder(cnddtId, careerOrder)) {
                Career career = new Career();
                career.setCnddtId(cnddtId);
                career.setCareerOrder(careerOrder);
                career.setCareer(careerDetail);

                careerService.saveCareer(career);
                System.out.println("Saved Career: " + careerDetail);
            } else {
                System.out.println("Duplicate or empty Career detected for Candidate ID: " + cnddtId + ", Order: " + careerOrder);
            }

            careerOrder++;
        }
    }
}
