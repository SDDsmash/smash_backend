package SDD.smash.Dwelling.Converter;

import SDD.smash.Dwelling.Dto.DwellingDTO;


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

}
