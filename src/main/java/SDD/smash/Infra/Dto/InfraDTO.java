package SDD.smash.Infra.Dto;


import lombok.Getter;

@Getter
public class InfraDTO {
    // 시군구 코드
    private String sigungu_code;
    // 직종 코드
    private String openSvcId;
    // 직종 개수
    private String num;

    public InfraDTO(String sigungu_code, String openSvcId, String num) {
        this.sigungu_code = sigungu_code;
        this.openSvcId = openSvcId;
        this.num = num;
    }

}
