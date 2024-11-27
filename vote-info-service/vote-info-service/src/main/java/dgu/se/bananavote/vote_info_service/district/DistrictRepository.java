package dgu.se.bananavote.vote_info_service.district;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    List<District> findByWiwName(String wiwName);
    List<District> findBySdName(String sdName);

    boolean existsBySdNameAndWiwNameAndSggName(String sdName, String wiwName, String sggName);
}
