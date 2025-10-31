package SDD.smash.Dwelling.Converter;

import SDD.smash.Dwelling.Dto.RentRecord;
import com.fasterxml.jackson.databind.JsonNode;

import static SDD.smash.Util.BatchTextUtil.nullZero;
import static SDD.smash.Util.MapperUtil.num;
import static SDD.smash.Util.MapperUtil.text;

public class RentRecordConverter {

    public static RentRecord toRecord(JsonNode node){
        String aptNm = text(node, "aptNm", "아파트");
        String jibun = text(node, "jibun", "지번");
        Integer deposit = num(node, "deposit", "보증금액");      // 보증금(천원 단위)
        Integer monthly = num(node, "monthlyRent", "월세금액");      // 월세(천원 단위/월)

        return RentRecord.builder()
                .aptNm(aptNm)
                .jibun(jibun)
                .deposit(nullZero(deposit))
                .monthlyRent(nullZero(monthly))
                .build();
    }
}
