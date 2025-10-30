package SDD.smash.Dwelling.Converter;

import SDD.smash.Address.Entity.Sigungu;
import SDD.smash.Address.Repository.SigunguRepository;
import SDD.smash.Dwelling.Dto.DwellingDTO;
import SDD.smash.Dwelling.Entity.Dwelling;


import java.util.List;


import static SDD.smash.Util.CalculateUtil.mean;
import static SDD.smash.Util.CalculateUtil.median;

public class DwellingConverter {


    public static DwellingDTO toDTO(List<Integer> monthValues, List<Integer> jeonseValues, String sigunguCode){
        return DwellingDTO.builder()
                .monthAvg(mean(monthValues))
                .monthMid(median(monthValues))
                .jeonseAvg(mean(jeonseValues))
                .jeonseMid(median(jeonseValues))
                .sigunguCode(sigunguCode)
                .build();
    }

    public static Dwelling toEntity(DwellingDTO dto,SigunguRepository sigunguRepository) throws IllegalAccessException {
        String sigunguCode = dto.getSigunguCode();
        Sigungu sigungu = sigunguRepository.findBySigunguCode(sigunguCode);
        if(sigungu == null){
            throw new IllegalAccessException("존재하지 않는 시군구 코드 : " + sigunguCode);
        }
        return Dwelling.builder()
                .sigungu(sigungu)
                .monthAvg(dto.getMonthAvg())
                .monthMid(dto.getMonthMid())
                .jeonseAvg(dto.getJeonseAvg())
                .jeonseMid(dto.getJeonseMid())
                .build();
    }
}
