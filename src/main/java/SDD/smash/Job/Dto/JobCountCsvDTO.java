package SDD.smash.Job.Dto;

import lombok.Getter;

@Getter
public class JobCountCsvDTO {
    private String sigungu_code;
    private String middle_code;
    private Integer count;

    public JobCountCsvDTO(String sigungu_code, String middle_code, Integer count) {
        this.sigungu_code = sigungu_code;
        this.middle_code = middle_code;
        this.count = count;
    }
}
