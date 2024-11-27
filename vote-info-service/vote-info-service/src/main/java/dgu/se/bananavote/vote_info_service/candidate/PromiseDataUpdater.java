package dgu.se.bananavote.vote_info_service.candidate;//package dgu.se.bananavote.vote_info_service.candidate;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.HttpServerErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//@Service
//public class PromiseDataUpdater {
//
//    private final PromiseRepository promiseRepository;
//    private final CandidateService candidateService;
//    private final RestTemplate restTemplate;
//    private final ObjectMapper objectMapper;
//
//    @Value("${api.serviceKey}")
//    private String serviceKey;
//
//    private final String API_URL_TEMPLATE = "https://apis.data.go.kr/9760000/ElecPrmsInfoInqireService/getCnddtElecPrmsInfoInqire" +
//            "?resultType=json&serviceKey=%s&pageNo=%d&numOfRows=100&sgId=%s&sgTypecode=%s&cnddtId=%s";
//
//    // 고정된 값
//    private static final String SG_ID = "20240410";
//    private static final String SG_TYPECODE = "2";
//
//    @Autowired
//    public PromiseDataUpdater(PromiseRepository promiseRepository,
//                              CandidateService candidateService,
//                              RestTemplate restTemplate,
//                              ObjectMapper objectMapper) {
//        this.promiseRepository = promiseRepository;
//        this.candidateService = candidateService;
//        this.restTemplate = restTemplate;
//        this.objectMapper = objectMapper;
//    }
//
//    public void updatePromiseData() {
//        List<Candidate> candidates = candidateService.getAllCandidates(); // 모든 후보자를 가져옵니다.
//
//        for (Candidate candidate : candidates) {
//            String cnddtId = candidate.getCnddtId(); // Candidate 엔터티에서 cnddtId를 가져옵니다.
//
//            int pageNo = 1;
//            int totalPages = 1;
//
//            try {
//                while (pageNo <= totalPages) {
//                    // URL 구성 및 URI로 변환
//                    String encodedCnddtId = URLEncoder.encode(cnddtId, StandardCharsets.UTF_8);
//                    String apiUrl = String.format(API_URL_TEMPLATE, serviceKey, pageNo, SG_ID, SG_TYPECODE, encodedCnddtId);
//                    URI uri = new URI(apiUrl);
//
//                    // 호출 간격 두기
////                    try {
////                        TimeUnit.SECONDS.sleep(2);
////                    } catch (InterruptedException e) {
////                        Thread.currentThread().interrupt();
////                    }
//
//                    // API 호출
//                    String jsonResponse = restTemplate.getForObject(uri, String.class);
//
//                    // JSON 응답 파싱
//                    JsonNode root = objectMapper.readTree(jsonResponse);
//                    JsonNode header = root.path("response").path("header");
//                    String resultCode = header.path("resultCode").asText();
//                    String resultMsg = header.path("resultMsg").asText();
//
//                    // 응답 결과 확인
//                    if (!"INFO-00".equals(resultCode)) {
//                        System.err.println("Unexpected response code: " + resultCode + " - " + resultMsg);
//                        break;
//                    }
//
//                    JsonNode body = root.path("response").path("body");
//                    JsonNode items = body.path("items").path("item");
//
//                    if (pageNo == 1) {
//                        int totalCount = body.path("totalCount").asInt();
//                        int numOfRows = body.path("numOfRows").asInt();
//                        totalPages = (int) Math.ceil((double) totalCount / numOfRows);
//                    }
//
//                    // 공약 데이터 처리
//                    for (JsonNode item : items) {
//                        int prmsCnt = item.path("prmsCnt").asInt();
//
//                        for (int i = 1; i <= prmsCnt; i++) {
//                            String promiseOrderField = "prmsOrd" + i;
//                            String promiseRealmField = "prmsRealmName" + i;
//                            String promiseTitleField = "prmsTitle" + i;
//                            String promiseContentField = "prmsCont" + i;
//
//                            // 공약 객체 생성 및 데이터 저장
//                            Promise promise = new Promise();
//                            promise.setCnddtId(cnddtId);
//                            promise.setPromiseOrder(item.path(promiseOrderField).asInt());
//                            promise.setPromiseTitle(item.path(promiseTitleField).asText());
//                            promise.setPromiseContent(item.path(promiseContentField).asText());
//
//                            // 리포지토리를 통한 공약 저장
//                            promiseRepository.save(promise);
//                        }
//                    }
//
//                    pageNo++;
//                }
//            } catch (URISyntaxException e) {
//                System.err.println("URI Syntax Error: " + e.getMessage());
//            } catch (HttpClientErrorException e) {
//                System.err.println("HTTP Client Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
//            } catch (HttpServerErrorException e) {
//                System.err.println("HTTP Server Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
//            } catch (Exception e) {
//                System.err.println("Unexpected Error: " + e.getMessage());
//                e.printStackTrace();
//            }
//        }
//    }
//}
