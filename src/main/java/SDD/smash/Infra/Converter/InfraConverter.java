package SDD.smash.Infra.Converter;

import SDD.smash.Address.Entity.Sigungu;
import SDD.smash.Infra.Dto.IndustryDTO;
import SDD.smash.Infra.Dto.InfraDTO;
import SDD.smash.Infra.Entity.Industry;
import SDD.smash.Infra.Entity.Infra;
import SDD.smash.Infra.Entity.Major;

import static SDD.smash.Util.BatchTextUtil.normalize;

public class InfraConverter {
    public static Industry industryToEntity(IndustryDTO dto){
        return Industry.builder()
                .code(normalize(dto.getCode()))
                .name(normalize(dto.getName()))
                .major(Major.valueOf(normalize(dto.getMajor())))
                .build();
    }
    public static Infra infraToEntity(InfraDTO dto, Sigungu sigungu, Industry industry){
        return Infra.builder()
                .count(Integer.parseInt(normalize(dto.getNum())))
                .sigungu(sigungu)
                .industry(industry)
                .build();
    }
}
