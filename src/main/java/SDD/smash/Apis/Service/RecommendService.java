package SDD.smash.Apis.Service;

import SDD.smash.Address.Repository.SigunguRepository;
import SDD.smash.Apis.Dto.CodeNameDTO;
import SDD.smash.Apis.Dto.RecommendDTO;
import SDD.smash.Apis.Dto.ScoreDTO;
import SDD.smash.Dwelling.Entity.DwellingType;
import SDD.smash.Dwelling.Service.DwellingScoreSerivce;
import SDD.smash.Dwelling.Service.DwellingService;
import SDD.smash.Infra.Entity.InfraImportance;
import SDD.smash.Infra.Service.InfraScoreService;
import SDD.smash.Infra.Service.InfraService;
import SDD.smash.Job.Service.JobScoreService;
import SDD.smash.Job.Service.JobService;
import SDD.smash.Support.domain.SupportTag;
import SDD.smash.Support.service.SupportScoreService;
import SDD.smash.Support.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final JobScoreService jobScoreService;
    private final DwellingScoreSerivce dwellingScoreService;
    private final SupportScoreService supportScoreService;
    private final InfraScoreService infraScoreService;

    private final JobService jobService;
    private final DwellingService dwellingService;
    private final SupportService supportService;
    private final InfraService infraService;

    private final SigunguRepository sigunguRepository;

    @Transactional(readOnly = true)
    public List<RecommendDTO> recommend(
            SupportTag supportTag,
            String midJobCode,
            DwellingType dwellingType, //필수
            Integer price, //필수
            InfraImportance infraImportance //필수
    )
    {
        Map<String, Integer> jobScoreMap = jobScoreService.getJobScore(midJobCode);
        Map<String, Integer> dwellingScoreMap = dwellingScoreService.getDwellingScoreByType(dwellingType, price);
        Map<String, Integer> supportScoreMap = supportScoreService.getSupportScoresByTag(supportTag);
        Map<String, Integer> infraScoreMap = infraScoreService.getInfraScoresByImportance(infraImportance);

        List<CodeNameDTO> codeNames = sigunguRepository.findAllCodeNames();

        List<ScoreDTO> scores = new ArrayList<>();

        int div = (supportTag == null) ? 3 : 4;
        for(CodeNameDTO dto : codeNames)
        {
            String code = dto.getSigunguCode();
            Integer jobScore = jobScoreMap.getOrDefault(code, 0);
            Integer dwellingScore = dwellingScoreMap.getOrDefault(code, 0);
            Integer supportScore = supportScoreMap.getOrDefault(code, 0);
            Integer infraScore = infraScoreMap.getOrDefault(code, 0);

            int sum = jobScore + dwellingScore + supportScore + infraScore;

            scores.add(new ScoreDTO(dto.getSidoCode(), dto.getSidoName(), dto.getSigunguCode(), dto.getSigunguName(),
                    (Integer)(sum / div)));
        }

        scores.sort((a,b) -> b.getScore().compareTo(a.getScore()));

        List<ScoreDTO> top10 = scores.size() > 10 ? scores.subList(0, 10) : scores;

        List<RecommendDTO> result = new ArrayList<>();
        for(ScoreDTO dto : top10)
        {
            String sigunguCode = dto.getSigunguCode();
            Integer score = dto.getScore();

            RecommendDTO rd = RecommendDTO.builder()
                    .sidoCode(dto.getSidoCode())
                    .sidoName(dto.getSidoName())
                    .sigunguCode(dto.getSigunguCode())
                    .sigunguName(dto.getSigunguName())
                    .score(score)

                    .totalJobInfo(jobService.getJobInfoBySigungu(sigunguCode))
                    .fitJobInfo(jobService.getJobInfoBySigunguAndJobCode(sigunguCode, midJobCode)) //jobCode 없는 경우 null

                    .totalSupportNum(supportService.getAllSupportNum(sigunguCode))
                    .fitSupportNum(supportService.getFitSupportNum(sigunguCode, supportTag)) //tag없는 경우 null

                    .dwellingSimpleInfo(dwellingService.getDwellingSimpleInfo(sigunguCode))

                    .infraMajors(infraService.getMajorInfraNum(sigunguCode))
                    .build();
            result.add(rd);
        }
        return result;
    }


}
