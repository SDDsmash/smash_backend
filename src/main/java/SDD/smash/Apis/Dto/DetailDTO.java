package SDD.smash.Apis.Dto;

import SDD.smash.Dwelling.Dto.DwellingInfoDTO;
import SDD.smash.Infra.Dto.InfraDetails;
import SDD.smash.Job.Dto.JobInfoDTO;
import SDD.smash.Support.dto.SupportListDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DetailDTO {

    private String sidoCode;
    private String sidoName;

    private String sigunguCode;
    private String sigunguName;

    // 일자리
    private JobInfoDTO totalJobInfo;
    private JobInfoDTO fitJobInfo;

    //지원사업
    private Integer totalSupportNum;

    private SupportListDTO supportList;

    //주거
    private DwellingInfoDTO dwellingInfo;

    //인프라
    private List<InfraDetails> infraDetails;

}
