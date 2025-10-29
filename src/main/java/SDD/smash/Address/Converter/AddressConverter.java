package SDD.smash.Address.Converter;

import SDD.smash.Address.Dto.SidoDTO;
import SDD.smash.Address.Dto.SigunguDTO;
import SDD.smash.Address.Entity.Sido;
import SDD.smash.Address.Entity.Sigungu;

import static SDD.smash.Util.BatchUtil.clean;

public class AddressConverter {
    public static Sido sidoToEntity(SidoDTO dto){
        return Sido.builder()
                .name(dto.getName())
                .sidoCode(clean(dto.getSido_code()))
                .build();
    }

    public static Sigungu sigunguToEntity(SigunguDTO dto, Sido sido){
        return Sigungu.builder()
                .name(dto.getName())
                .sigunguCode(dto.getSigungu_code())
                .sido(sido)
                .build();

    }
}
