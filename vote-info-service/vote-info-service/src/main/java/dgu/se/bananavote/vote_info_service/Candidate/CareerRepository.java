package dgu.se.bananavote.vote_info_service.Candidate;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CareerRepository extends JpaRepository<Career, Integer> {
    List<Career> findByCnddtId(String cnddtId);
}
