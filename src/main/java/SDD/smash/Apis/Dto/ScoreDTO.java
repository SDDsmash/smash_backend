package SDD.smash.Apis.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScoreDTO {
    private String sidoCode;
    private String sidoName;

    private String sigunguCode;
    private String sigunguName;

    private Integer score;
}
