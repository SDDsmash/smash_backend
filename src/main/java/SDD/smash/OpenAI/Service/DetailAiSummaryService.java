package SDD.smash.OpenAI.Service;

import SDD.smash.Apis.Dto.DetailDTO;
import SDD.smash.Apis.Dto.DetailResponseDTO;
import SDD.smash.OpenAI.Client.OpenAiClient;
import SDD.smash.OpenAI.Converter.SummaryConverter;
import SDD.smash.OpenAI.Dto.OpenAiMessage;
import SDD.smash.OpenAI.Dto.OpenAiRequest;
import SDD.smash.OpenAI.Dto.OpenAiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetailAiSummaryService {
    private final OpenAiClient openAiClient;
    private final ObjectMapper objectMapper;
    private final String MODEL;


    public DetailAiSummaryService(OpenAiClient openAiClient, ObjectMapper objectMapper,
                                  @Value("${openai.model}") String model) {
        this.openAiClient = openAiClient;
        this.objectMapper = objectMapper;
        this.MODEL = model;

    }

    public DetailResponseDTO summarize(DetailDTO dto){
        try{
            String json = objectMapper.writeValueAsString(dto);


            // 1) system 메시지: 규칙/톤 지시
            OpenAiMessage system = new OpenAiMessage(
                    "system",
                            "모든 질문에는 친절한 AI 비서로서 답변해주세요." +
                            "응답은 한국어로, 간결하고 사실 기반으로 작성하세요."
            );

            String userPrompt = """
            아래는 특정 지역의 상세 데이터(JSON)입니다.
            이 정보를 바탕으로 '사람이 이해하기 쉬운 한국어 요약'을 작성하세요.

            작성 규칙:
            - 한 문단 핵심 요약(한 줄)
            - 장점(2~4개 불릿)
            - 일자리/지원/주거/인프라 핵심 포인트 표(열: 항목 | 요약)
            - 과장/추정 금지, 데이터 없는 사실 배제
            - 상대점수/비율은 '상대 평가'임을 명시

            [지역 상세 JSON]
            ```json
            %s
            ```
            """.formatted(json);
            OpenAiMessage user = new OpenAiMessage("user", userPrompt);
            OpenAiRequest request = new OpenAiRequest(MODEL, List.of(system, user));

            OpenAiResponse response = openAiClient.getChatCompletion(request);

            String aiSummaryContent = response.getChoices().get(0).getMessage().getContent();

            return SummaryConverter.toResponseDTO(dto, aiSummaryContent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
