package SDD.smash.Infra.Converter;

import SDD.smash.Infra.Dto.JobCodeMiddleDTO;
import SDD.smash.Infra.Dto.JobCodeTopDTO;
import SDD.smash.Infra.Entity.JobCodeMiddle;
import SDD.smash.Infra.Entity.JobCodeTop;

import static SDD.smash.Util.BatchUtil.addLeadingZero;
import static SDD.smash.Util.BatchUtil.clean;

public class InfraConverter {
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
