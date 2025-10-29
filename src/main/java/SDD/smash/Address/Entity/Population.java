package SDD.smash.Address.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Population {

    @Id
    @Column(name = "sigungu_code", length = 5)
    private String sigunguCode;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "sigungu_code")
    private Sigungu sigungu;

    @Column(name = "population_count")
    @NotNull
    private Integer populationCount;
}
