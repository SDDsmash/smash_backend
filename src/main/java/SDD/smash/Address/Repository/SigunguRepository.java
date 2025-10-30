package SDD.smash.Address.Repository;

import SDD.smash.Address.Entity.Sigungu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SigunguRepository extends JpaRepository<Sigungu,String> {
    Sigungu findBySigunguCode(String sigunguCode);

}
