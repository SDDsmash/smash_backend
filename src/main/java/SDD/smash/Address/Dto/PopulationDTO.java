package SDD.smash.Address.Dto;

import lombok.Getter;

@Getter
public class PopulationDTO {
    private String sigungu_code;
    private String population;

    public PopulationDTO(String sigungu_code, String population) {
        this.sigungu_code = sigungu_code;
        this.population = population;
    }
}
