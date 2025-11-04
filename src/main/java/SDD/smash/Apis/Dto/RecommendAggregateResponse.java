package SDD.smash.Apis.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecommendAggregateResponse {
    private List<RecommendDTO> items; // 기존 추천 리스트
    private List<AiPickEntry> aiPick;         // AI 추천 3개
}