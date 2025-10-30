package SDD.smash.Dwelling.Service;

import SDD.smash.Address.Repository.SigunguRepository;
import SDD.smash.Dwelling.Adapter.MolitAptRentAdapter;
import SDD.smash.Dwelling.Converter.DwellingConverter;
import SDD.smash.Dwelling.Dto.DwellingDTO;
import SDD.smash.Dwelling.Dto.RentRecord;
import SDD.smash.Dwelling.Dto.ResponseDTO;
import SDD.smash.Dwelling.Entity.Dwelling;
import SDD.smash.Dwelling.Repository.DwellingRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static SDD.smash.Dwelling.Converter.DwellingConverter.toDTO;
import static SDD.smash.Dwelling.Converter.DwellingConverter.toEntity;

@Service
@Slf4j
public class DwellingStatesService {
    private final MolitAptRentAdapter adapter;
    private final SigunguRepository sigunguRepository;
    private final DwellingRepository dwellingRepository;

    public DwellingStatesService(MolitAptRentAdapter adapter, SigunguRepository sigunguRepository, DwellingRepository dwellingRepository) {
        this.adapter = adapter;
        this.sigunguRepository = sigunguRepository;
        this.dwellingRepository = dwellingRepository;
    }

    @Transactional
    public ResponseDTO getStats(String sigunguCode, String dealYmd, int months) throws IllegalAccessException {
        YearMonth to = YearMonth.parse(dealYmd, DateTimeFormatter.ofPattern("yyyyMM"));
        YearMonth from = to.minusMonths(months - 1);

        List<RentRecord> arrayList = new ArrayList<>();
        for (YearMonth ym = from; !ym.isAfter(to); ym = ym.plusMonths(1)) {
            List<RentRecord> records = adapter.fetchMonth(sigunguCode, ym, 1, 1000);
            arrayList.addAll(records);
            if(records.isEmpty()){
                log.warn("YM={} records: size=0", ym);
            }
            log.info("YM={} records: size={}, first={}", ym, records.size(), records.get(0));
        }
        List<Integer> monthValues = getMonths(arrayList);
        List<Integer> jeonseValues = getJeonse(arrayList);
        if(monthValues.isEmpty() && jeonseValues.isEmpty()){
            log.warn("No rent data to aggregate for sigunguCode={}", sigunguCode);
        }
        DwellingDTO dto = toDTO(monthValues, jeonseValues, sigunguCode);

        Dwelling dwelling = toEntity(dto, sigunguRepository);
        try{
            dwellingRepository.save(dwelling);
        } catch (Exception e) {
            throw new IllegalAccessException("저장하지 못했습니다." + e);
        }

        return new ResponseDTO(true);
    }


    private static List<Integer> getMonths(List<RentRecord> arrayList) {
        List<Integer> months = new ArrayList<>();
        for (RentRecord record : arrayList) {
            Integer monthlyRent = record.getMonthlyRent();
            if (monthlyRent != null && monthlyRent > 0) {
                months.add(monthlyRent);
            }
        }
        return months;
    }

    private static List<Integer> getJeonse(List<RentRecord> arrayList) {
        List<Integer> jeonseDeposits = new ArrayList<>();
        for (RentRecord record : arrayList) {
            Integer monthlyRent = record.getMonthlyRent();
            if (monthlyRent != null && monthlyRent == 0) {
                jeonseDeposits.add(record.getDeposit());
            }
        }
        return jeonseDeposits;
    }
}
