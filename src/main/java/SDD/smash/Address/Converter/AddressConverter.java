package SDD.smash.Address.Converter;

import SDD.smash.Address.Dto.PopulationDTO;
import SDD.smash.Address.Dto.SidoDTO;
import SDD.smash.Address.Dto.SigunguDTO;
import SDD.smash.Address.Entity.Population;
import SDD.smash.Address.Entity.Sido;
import SDD.smash.Address.Entity.Sigungu;

import static SDD.smash.Util.BatchTextUtil.normalize;

public class AddressConverter {
    public static Sido sidoToEntity(SidoDTO dto){
        return Sido.builder()
                .name(normalize(dto.getName()))
                .sidoCode(normalize(dto.getSido_code()))
                .build();
    }

    public static Sigungu sigunguToEntity(SigunguDTO dto, Sido sido){
        return Sigungu.builder()
                .name(normalize(dto.getName()))
                .sigunguCode(normalize(dto.getSigungu_code()))
                .sido(sido)
                .build();
    }

    public static Population populationToEntity(PopulationDTO dto, Sigungu sigungu){
        return Population.builder()
                .sigungu(sigungu)
                .populationCount(Integer.parseInt(normalize(dto.getPopulation())))
                .build();
    }
}
