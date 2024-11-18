package dgu.se.bananavote.vote_info_service.candidate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareerRepository extends JpaRepository<Career, Integer> {
    List<Career> findByCnddtId(String cnddtId);

    boolean existsByCnddtIdAndCareerOrder(String cnddtId, int careerOrder);
}
