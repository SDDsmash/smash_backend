package SDD.smash.Dwelling.Service;

import SDD.smash.Address.Entity.Sigungu;
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
    public ResponseDTO getStatsAndSave(String sigunguCode, String dealYmd, int months) throws IllegalAccessException {
        YearMonth to = YearMonth.parse(dealYmd, DateTimeFormatter.ofPattern("yyyyMM"));
        YearMonth from = to.minusMonths(months - 1);

        List<RentRecord> arrayList = new ArrayList<>();
        for (YearMonth ym = from; !ym.isAfter(to); ym = ym.plusMonths(1)) {
            List<RentRecord> records = adapter.fetchMonth(sigunguCode, ym, 1, 1000);
            arrayList.addAll(records);
            if(records.isEmpty()){
                log.warn("YM={} records: size=0", ym);
            }
        }
        List<Integer> monthValues = getMonths(arrayList);
        List<Integer> jeonseValues = getJeonse(arrayList);
        if(monthValues.isEmpty() && jeonseValues.isEmpty()){
            log.warn("No rent data to aggregate for sigunguCode={}", sigunguCode);
        }
        DwellingDTO dto = toDTO(monthValues, jeonseValues, sigunguCode);

        Dwelling dwelling = getDwelling(sigunguCode, dto);

        dwellingRepository.save(dwelling);
        return new ResponseDTO(true, "성공적으로 저장됐습니다.");
    }

    private Dwelling getDwelling(String sigunguCode, DwellingDTO dto) {
        Sigungu sigungu = sigunguRepository.getReferenceById(sigunguCode);

        Dwelling dwelling = dwellingRepository.findBySigungu_SigunguCode(sigunguCode)
                .orElseGet(() -> {
                    Dwelling d = new Dwelling(); // id=null → 새 엔티티
                    d.setSigungu(sigungu);
                    return d;
                });

        dwelling.setMonthAvg(dto.getMonthAvg());
        dwelling.setMonthMid(dto.getMonthMid());
        dwelling.setJeonseAvg(dto.getJeonseAvg());
        dwelling.setJeonseMid(dto.getJeonseMid());
        return dwelling;
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
