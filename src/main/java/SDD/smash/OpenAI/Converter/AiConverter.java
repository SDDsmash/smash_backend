package SDD.smash.OpenAI.Converter;

import SDD.smash.Apis.Dto.*;
import SDD.smash.OpenAI.Dto.AiRecommendDTO;
import org.springframework.lang.Nullable;

import java.util.List;

public class AiConverter {
    public static DetailResponseDTO toResponseDTO(DetailDTO dto, @Nullable String summarizeContent){
        return DetailResponseDTO.builder()
                .sidoCode(dto.getSidoCode())
                .sidoName(dto.getSidoName())
                .sigunguCode(dto.getSigunguCode())
                .sigunguName(dto.getSigunguName())
                .population(dto.getPopulation())
                .totalJobInfo(dto.getTotalJobInfo())
                .fitJobInfo(dto.getFitJobInfo())
                .totalSupportNum(dto.getTotalSupportNum())
                .supportList(dto.getSupportList())
                .dwellingInfo(dto.getDwellingInfo())
                .infraDetails(dto.getInfraDetails())
                .aiSummary(summarizeContent)
                .build();
    }

    public static RecommendAggregateResponse toResponseList(List<RecommendDTO> recommendDTOList,
                                                            @Nullable AiRecommendDTO aiRecommendDTO){
        List<RecommendDTO> items = recommendDTOList.stream()
                .map(dto -> RecommendDTO.builder()
                        .sidoCode(dto.getSidoCode())
                        .sidoName(dto.getSidoName())
                        .sigunguCode(dto.getSigunguCode())
                        .sigunguName(dto.getSigunguName())
                        .score(dto.getScore())
                        .totalJobInfo(dto.getTotalJobInfo())
                        .fitJobInfo(dto.getFitJobInfo())
                        .totalSupportNum(dto.getTotalSupportNum())
                        .fitSupportNum(dto.getFitSupportNum())
                        .dwellingSimpleInfo(dto.getDwellingSimpleInfo())
                        .infraMajors(dto.getInfraMajors())
                        .build())
                .toList();

        List<AiPickEntry> aiPick = (aiRecommendDTO == null || aiRecommendDTO.getRecommendations() == null)
                ? List.of()
                : aiRecommendDTO.getRecommendations().stream()
                .map(p -> AiPickEntry.builder()
                        .aiPickSigunguCode(p.getSigunguCode())
                        .aiPickReason(p.getReason())
                        .build())
                .toList();

        return RecommendAggregateResponse.builder()
                .items(items)
                .aiPick(aiPick)
                .build();
    }
}
