package SDD.smash.Dwelling.Service;

import SDD.smash.Address.Repository.SigunguRepository;
import SDD.smash.Dwelling.Adapter.MolitAptRentAdapter;
import SDD.smash.Dwelling.Converter.DwellingConverter;
import SDD.smash.Dwelling.Dto.DwellingDTO;
import SDD.smash.Dwelling.Dto.RentRecord;
import SDD.smash.Dwelling.Entity.Dwelling;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DwellingStatesService {
    private final MolitAptRentAdapter adapter;
    private final SigunguRepository sigunguRepository;

    public DwellingStatesService(MolitAptRentAdapter adapter, SigunguRepository sigunguRepository) {
        this.adapter = adapter;
        this.sigunguRepository = sigunguRepository;
    }

    public Dwelling getStats(String sigunguCode, String dealYmd, int months){
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
        DwellingDTO dto = DwellingConverter.toDTO(monthValues, jeonseValues, sigunguCode);

        return DwellingConverter.toEntity(dto,sigunguRepository);
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
