package dgu.se.bananavote.vote_info_service.District;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Integer> {
    List<District> findByWiwName(String wiwName);
    List<District> findBySdName(String sdName);
}
