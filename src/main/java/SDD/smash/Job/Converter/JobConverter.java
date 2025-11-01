package SDD.smash.Job.Converter;

import SDD.smash.Job.Dto.JobCodeMiddleDTO;
import SDD.smash.Job.Dto.JobCodeTopDTO;
import SDD.smash.Job.Entity.JobCodeMiddle;
import SDD.smash.Job.Entity.JobCodeTop;

import static SDD.smash.Util.BatchTextUtil.*;

public class JobConverter {
    public static JobCodeTop topToEntity(JobCodeTopDTO dto){
        return JobCodeTop.builder()
                .code(addLeadingZero(normalize(dto.getCode())))
                .name(normalize(dto.getName()))
                .build();
    }

    public static JobCodeMiddle middleToEntity(JobCodeMiddleDTO dto, JobCodeTop jobCodeTop) {
        return JobCodeMiddle.builder()
                .code(addLeadingZeroThird(normalize(dto.getCode())))
                .name(normalize(dto.getName()))
                .jobCodeTop(jobCodeTop)
                .build();
    }

}
