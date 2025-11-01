package SDD.smash.Infra.Repository;

import SDD.smash.Address.Entity.Sigungu;
import SDD.smash.Infra.Entity.Industry;
import SDD.smash.Infra.Dto.InfraDetails;
import SDD.smash.Infra.Dto.InfraMajor;
import SDD.smash.Infra.Entity.Infra;
import SDD.smash.Infra.Entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import java.util.List;

@Repository
public interface InfraRepository extends JpaRepository<Infra,Long> {
    Optional<Infra> findBySigunguAndIndustry(Sigungu sigungu, Industry industry);

    @Query("""
    SELECT new SDD.smash.Infra.Dto.InfraMajor(
        ind.major,
        SUM(i.count)
    )
    FROM Infra i
    JOIN i.industry ind
    WHERE i.sigungu.sigunguCode = :sigunguCode
      AND ind.major = :major
    GROUP BY ind.major
""")
    Optional<InfraMajor> getInfraMajor(@Param("sigunguCode") String sigunguCode,
                                       @Param("major") Major major);

    @Query("""
    SELECT new SDD.smash.Infra.Dto.InfraDetails(
        ind.major,
        ind.name,
        i.count
    )
    FROM Infra i
    JOIN i.industry ind
    WHERE i.sigungu.sigunguCode = :sigunguCode
""")
    List<InfraDetails> getInfraDetails(String sigunguCode);
}
