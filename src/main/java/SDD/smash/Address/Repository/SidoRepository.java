package SDD.smash.Address.Repository;


import SDD.smash.Address.Entity.Sido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SidoRepository extends JpaRepository<Sido,String> {

}
