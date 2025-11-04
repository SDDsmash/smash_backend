package SDD.smash.OpenAI.Converter;

import SDD.smash.Apis.Dto.DetailDTO;
import SDD.smash.Apis.Dto.DetailResponseDTO;

public class SummaryConverter {
    public static DetailResponseDTO toResponseDTO(DetailDTO dto, String summarizeContent){
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
}
