package SDD.smash.Dwelling.Entity;

import SDD.smash.Address.Entity.Sigungu;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Dwelling {
    @Id
    @Column(name = "sigungu_code", length = 5)
    private String sigunguCode;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "sigungu_code")
    private Sigungu sigungu;

    @Column(name = "month_avg")
    private Double monthAvg;
    @Column(name = "month_mid")
    private Integer monthMid;

    @Column(name = "jeonse_avg")
    private Double jeonseAvg;
    @Column(name = "jeonse_mid")
    private Integer jeonseMid;

}
