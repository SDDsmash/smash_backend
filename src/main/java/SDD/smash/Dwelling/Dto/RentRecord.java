package SDD.smash.Dwelling.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RentRecord {
    private String aptNm;
    private String jibun;
    private Integer deposit;
    private Integer monthlyRent;
}
