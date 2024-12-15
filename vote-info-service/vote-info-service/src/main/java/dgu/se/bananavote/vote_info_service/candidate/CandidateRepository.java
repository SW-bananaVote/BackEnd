package dgu.se.bananavote.vote_info_service.candidate;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Integer> {
    // findById는 기본 제공
    //Optional<Candidate> findById(Integer id);
    List<Candidate> findByJdName(String jdName);
    List<Candidate> findByWiwName(String wiwName);
    List<Candidate> findByName(String name);
    List<Candidate> findByWiwNameAndJdName(String wiwName, String jdName); // 선거구와 정당으로 필터링
    List<Candidate> findByWiwNameAndName(String wiwName, String name); // 선거구와 이름으로 필터링
    List<Candidate> findByJdNameAndName(String jdName, String name); // 정당과 이름으로 필터링
    List<Candidate> findByWiwNameAndJdNameAndName(String wiwName, String jdName, String name); // 선거구, 정당, 이름으로 필터링

    boolean existsByCnddtId(String cnddtId);

    List<Candidate> findByJdNameIn(List<String> partyNames);
}
