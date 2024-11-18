package dgu.se.bananavote.vote_info_service.district;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class DistrictDataUpdater {

    private final DistrictService districtService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // Service key injected from application.properties
    @Value("${api.serviceKey}")
    private String serviceKey;

    // URL template
    private final String API_URL_TEMPLATE = "https://apis.data.go.kr/9760000/CommonCodeService/getCommonSggCodeList" +
            "?resultType=json&serviceKey=%s&pageNo=%d&numOfRows=100&sgId=20240410&sgTypecode=2";

    @Autowired
    public DistrictDataUpdater(DistrictService districtService, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.districtService = districtService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public void updateDistrictData() {
        int pageNo = 1;
        int totalPages = 1;

        try {
            while (pageNo <= totalPages) {
                // Encode the URL by wrapping it in a URI object
                String apiUrl = String.format(API_URL_TEMPLATE, serviceKey, pageNo);
                URI uri = new URI(apiUrl);
                System.out.println(uri);

                // Fetch the data
                String jsonResponse = restTemplate.getForObject(uri, String.class);
                System.out.println("API Response: " + jsonResponse);

                // Parse the JSON
                JsonNode root = objectMapper.readTree(jsonResponse);
                JsonNode body = root.path("response").path("body");
                JsonNode items = body.path("items").path("item");

                if (pageNo == 1) {
                    int totalCount = body.path("totalCount").asInt();
                    int numOfRows = body.path("numOfRows").asInt();
                    totalPages = (int) Math.ceil((double) totalCount / numOfRows);
                }

                for (JsonNode item : items) {
                    District district = new District();
                    district.setSdName(item.path("sdName").asText());
                    district.setWiwName(item.path("wiwName").asText());
                    district.setSggName(item.path("sggName").asText());
                    districtService.saveDistrict(district);
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