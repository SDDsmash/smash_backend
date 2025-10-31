package SDD.smash.Infra.Repository;

import SDD.smash.Infra.Entity.Infra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfraRepository extends JpaRepository<Infra,Long> {
}
