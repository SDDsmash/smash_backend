package SDD.smash.Address.Repository;

import SDD.smash.Address.Dto.SigunguCodeDTO;
import SDD.smash.Address.Entity.Sigungu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SigunguRepository extends JpaRepository<Sigungu,String> {
    Sigungu findBySigunguCode(String sigunguCode);

    @Query("SELECT new SDD.smash.Address.Dto.SigunguCodeDTO(s.sigunguCode) FROM Sigungu s")
    List<SigunguCodeDTO> findAllSigunguCodes();

    boolean existsBySigunguCode(String sigunguCode);

}
