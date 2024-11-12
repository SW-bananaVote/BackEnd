package dgu.se.bananavote.vote_info_service.Candidate;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromiseRepository extends JpaRepository<Promise, Integer> {
    List<Promise> findByCnddtId(String cnddtId);
}
