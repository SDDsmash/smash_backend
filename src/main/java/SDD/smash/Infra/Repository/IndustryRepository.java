package SDD.smash.Infra.Repository;

import SDD.smash.Infra.Entity.Industry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndustryRepository extends JpaRepository<Industry, String> {
}
