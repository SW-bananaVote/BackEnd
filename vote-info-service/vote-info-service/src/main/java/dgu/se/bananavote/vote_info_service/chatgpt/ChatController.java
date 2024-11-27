package dgu.se.bananavote.vote_info_service.chatgpt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Value("${OPENAI_API_KEY}")
    private String apiKey;

    @PostMapping
    public Map<String, Object> chat(@RequestBody Map<String, String> request) {
        String message = request.get("message");

        // OpenAI API URL
        String url = "https://api.openai.com/v1/chat/completions";

        // 요청 본문 생성
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", new Object[]{
                        Map.of("role", "user", "content", message)
                }
        );

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        // HTTP 요청
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // API 호출
        return restTemplate.postForObject(url, entity, Map.class);
    }
}