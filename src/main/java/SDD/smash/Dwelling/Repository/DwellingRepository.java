package SDD.smash.Dwelling.Repository;

import SDD.smash.Dwelling.Entity.Dwelling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DwellingRepository extends JpaRepository<Dwelling,Long> {
    Optional<Dwelling> findBySigungu_SigunguCode(String sigunguCode);
}
