package dgu.se.bananavote.vote_info_service.district;

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
import java.util.ArrayList;
import java.util.List;

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

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void updateDistrictData() {
        int pageNo = 1;
        int totalPages = 1;

        // List to collect all districts for saving to SQL file
        List<District> allDistricts = new ArrayList<>();

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
                    String sdName = item.path("sdName").asText();
                    String wiwName = item.path("wiwName").asText();
                    String sggName = item.path("sggName").asText();

                    // 중복 확인
                    if (!districtService.existsBySdNameAndWiwNameAndSggName(sdName, wiwName, sggName)) {
                        District district = new District();
                        district.setSdName(sdName);
                        district.setWiwName(wiwName);
                        district.setSggName(sggName);
                        districtService.saveDistrict(district); // Save to database
                        allDistricts.add(district); // Add to the list for SQL export
                        System.out.println("Saved District: " + sdName + ", " + wiwName + ", " + sggName);
                    } else {
                        System.out.println("Duplicate District detected: " + sdName + ", " + wiwName + ", " + sggName);
                    }
                }

                pageNo++;
            }

//            // Save all collected districts to SQL file
//            saveDataAsSql(allDistricts);

        } catch (URISyntaxException e) {
            System.err.println("URI Syntax Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void saveDataAsSql(Iterable<District> districts) {
//        String outputDir = "/app/output";
//        String filePath = outputDir + "/init_districts.sql";
//
//        try {
//            // Ensure the directory exists
//            File directory = new File(outputDir);
//            if (!directory.exists()) {
//                directory.mkdirs();
//            }
//
//            // Write the SQL file
//            try (FileWriter writer = new FileWriter(filePath)) {
//                writer.write("CREATE TABLE IF NOT EXISTS districts (\n" +
//                        "  id INT AUTO_INCREMENT PRIMARY KEY,\n" +
//                        "  sdName VARCHAR(255),\n" +
//                        "  wiwName VARCHAR(255),\n" +
//                        "  sggName VARCHAR(255)\n" +
//                        ");\n\n");
//
//                for (District district : districts) {
//                    writer.write(String.format(
//                            "INSERT INTO districts (sdName, wiwName, sggName) VALUES ('%s', '%s', '%s');\n",
//                            district.getSdName(), district.getWiwName(), district.getSggName()
//                    ));
//                }
//
//                System.out.println("Data saved as " + filePath);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
