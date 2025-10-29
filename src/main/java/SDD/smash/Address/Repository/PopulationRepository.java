package SDD.smash.Address.Repository;

import SDD.smash.Address.Entity.Population;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PopulationRepository extends JpaRepository<Population, String> {
}
