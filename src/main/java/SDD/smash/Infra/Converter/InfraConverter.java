package SDD.smash.Infra.Converter;

import SDD.smash.Infra.Dto.IndustryDTO;
import SDD.smash.Infra.Entity.Industry;
import SDD.smash.Infra.Entity.Major;

public class InfraConverter {
    public static Industry industryToEntity(IndustryDTO dto){
        return Industry.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .major(Major.valueOf(dto.getMajor()))
                .build();
    }
}
