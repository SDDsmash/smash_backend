package SDD.smash.Job.Dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobCountUpsertDTO {
    private String sigunguCode;
    private String middleCode;
    private Integer score;

    public JobCountUpsertDTO(String sigunguCode, String middleCode, Integer score) {
        this.sigunguCode = sigunguCode;
        this.middleCode = middleCode;
        this.score = score;
    }
}
