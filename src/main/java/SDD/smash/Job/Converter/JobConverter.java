package SDD.smash.Job.Converter;

import SDD.smash.Job.Dto.JobCodeMiddleDTO;
import SDD.smash.Job.Dto.JobCodeTopDTO;
import SDD.smash.Job.Entity.JobCodeMiddle;
import SDD.smash.Job.Entity.JobCodeTop;

import static SDD.smash.Util.BatchUtil.addLeadingZero;
import static SDD.smash.Util.BatchUtil.clean;

public class JobConverter {
    public static JobCodeTop topToEntity(JobCodeTopDTO dto){
        return JobCodeTop.builder()
                .code(addLeadingZero(clean(dto.getCode())))
                .name(clean(dto.getName()))
                .build();
    }

    public static JobCodeMiddle middleToEntity(JobCodeMiddleDTO dto, JobCodeTop entity) {
        return JobCodeMiddle.builder()
                .code(addLeadingZero(clean(dto.getCode())))
                .name(clean(dto.getName()))
                .jobCodeTop(entity)
                .build();
    }

}
