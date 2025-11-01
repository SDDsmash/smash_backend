package SDD.smash.Job.Dto;

import lombok.Getter;

@Getter
public class JobCountCsvDTO {
    private String sigungu_code;
    private String middle_code;
    private Integer score;

    public JobCountCsvDTO(String sigungu_code, String middle_code, Integer score) {
        this.sigungu_code = sigungu_code;
        this.middle_code = middle_code;
        this.score = score;
    }
}
