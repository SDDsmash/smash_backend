package SDD.smash.Dwelling.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DwellingDTO {

    // 시군구 코드
    private String sigunguCode;     

    // 월세 지표
    private Integer monthAvg;     // 평균(만원/월)
    private Integer monthMid;  // 중앙값(만원/월)

    // 전세 지표(보증금)
    private Integer jeonseAvg;    // 평균 보증금(만원)
    private Integer jeonseMid; // 중앙값 보증금(만원)

}
