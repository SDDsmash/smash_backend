package SDD.smash.Infra.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Industry {
    @Id
    @Column(name = "industry_code", length = 10, nullable = false)
    private String code;

    @NotNull
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Major major;
}
