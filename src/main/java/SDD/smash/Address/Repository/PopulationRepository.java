package SDD.smash.Address.Repository;

import SDD.smash.Address.Entity.Population;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PopulationRepository extends JpaRepository<Population, Long> {

    @Query("SELECT p.populationCount FROM Population p WHERE p.sigungu.sigunguCode = :sigunguCode")
    Optional<Integer> findPopulationCountBySigunguCode(@Param("sigunguCode") String sigunguCode);

}
