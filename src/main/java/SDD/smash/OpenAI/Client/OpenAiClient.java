package SDD.smash.OpenAI.Client;


import SDD.smash.OpenAI.Dto.OpenAiRequest;
import SDD.smash.OpenAI.Dto.OpenAiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Slf4j
public class OpenAiClient {
    private final RestTemplate restTemplate;
    private final String APIKEY;
    private final String APIURL;

    public OpenAiClient(RestTemplate restTemplate, @Value("${openai.api-key}") String apiKey,
                        @Value("${openai.api-url}") String apiUrl) {
        this.restTemplate = restTemplate;
        this.APIURL = apiUrl;
        this.APIKEY = apiKey;
    }
    /**
     * 사용자 질문을 GPT 모델에 전달하고 응답 받기.
     * */
    public OpenAiResponse getChatCompletion(OpenAiRequest requestDto) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);    // ✅ 반드시 JSON
        headers.setAccept(List.of(MediaType.APPLICATION_JSON)); // ✅ 응답도 JSON 기대
        headers.setBearerAuth(APIKEY);
        ObjectMapper om = new ObjectMapper();
        log.info("req.body={}", om.writeValueAsString(requestDto));

        HttpEntity<OpenAiRequest> entity = new HttpEntity<>(requestDto, headers);

        ResponseEntity<OpenAiResponse> res = restTemplate.postForEntity(
                APIURL, entity, OpenAiResponse.class);
        if (!res.getStatusCode().is2xxSuccessful() || res.getBody() == null) {
            throw new RuntimeException("OpenAI API 호출 실패: " + res.getStatusCode());
        }
        return res.getBody();
    }

}
