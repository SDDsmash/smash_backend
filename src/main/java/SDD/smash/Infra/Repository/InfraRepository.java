package SDD.smash.Infra.Repository;

import SDD.smash.Address.Entity.Sigungu;
import SDD.smash.Infra.Entity.Industry;
import SDD.smash.Infra.Entity.Infra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InfraRepository extends JpaRepository<Infra,Long> {
    Optional<Infra> findBySigunguAndIndustry(Sigungu sigungu, Industry industry);
}
