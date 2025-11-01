package SDD.smash.Address.Dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PopulationUpsertDTO {
    private String sigunguCode;
    private String population;

}
