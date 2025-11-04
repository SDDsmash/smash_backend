package SDD.smash.OpenAI.Service;

import SDD.smash.Apis.Dto.DetailDTO;
import SDD.smash.Apis.Dto.DetailResponseDTO;
import SDD.smash.OpenAI.Client.OpenAiClient;
import SDD.smash.OpenAI.Converter.AiConverter;
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
                이 정보를 바탕으로 사람이 읽기 쉬운 자연스러운 한국어 요약을 작성하세요.
                
                작성 규칙:
                1) 마크다운 문법은 절대 사용하지 마세요.
                   - 예: ```json, ```, `, *, -, +, #, |, [], (), **굵게**, *기울임*, [텍스트](링크), 1. 2. 3. 등
                   - 마크다운 줄바꿈(두 칸 공백 후 개행: '  \\n')도 금지합니다. 일반 개행(\\n)만 사용하세요.
                2) 출력은 오직 순수 텍스트입니다. 코드블록, 리스트, 표, 인용구, 링크, 헤딩 등은 포함하지 마세요.
                3) 출력 구조:
                   (1) 첫 문단: 핵심 요약 한 줄 (한 문장)
                   (2) 다음 줄들: 장점 2~4개 (번호 사용: ①, ②, ③, ④)
                   (3) 마지막 부분: 일자리/지원/주거/인프라 핵심 포인트 요약
                       - 항목별로 '항목명: 내용' 형식으로 작성 (표/파이프(|) 사용 금지)
                4) 과장/추정/감정 표현 금지. 데이터가 없는 사실은 배제하세요.
                5) 상대점수·비율 등은 '상대 평가'임을 반드시 명시하세요.
                
                입력 JSON 데이터:
                %s
                """.formatted(json);
            OpenAiMessage user = new OpenAiMessage("user", userPrompt);
            OpenAiRequest request = new OpenAiRequest(MODEL, List.of(system, user));

            OpenAiResponse response = openAiClient.getChatCompletion(request);

            String aiSummaryContent = response.getChoices().get(0).getMessage().getContent()
                    .replaceAll(" {2,}\\n", "\n") // 공백 2개+개행 → 일반 개행
                    .replaceAll("[ \t]+\\r?\\n", "\n") // 줄 끝 공백 제거
                    .trim();

            return AiConverter.toResponseDTO(dto, aiSummaryContent);
        } catch (JsonProcessingException e) {
            return AiConverter.toResponseDTO(dto,null);
        }
    }
}
