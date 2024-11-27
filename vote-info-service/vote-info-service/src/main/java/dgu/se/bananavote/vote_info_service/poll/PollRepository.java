package dgu.se.bananavote.vote_info_service.poll;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long>, JpaSpecificationExecutor<Poll> {
    boolean existsBySgIdAndPsNameAndAddr(String sgId, String psName, String addr);

    @Query("SELECT p FROM Poll p " +
            "WHERE (:sdName IS NULL OR p.sdName = :sdName) " +
            "AND (:wiwName IS NULL OR p.wiwName = :wiwName) " +
            "AND (:emdName IS NULL OR p.emdName = :emdName)")
    List<Poll> filterPoll(@Param("sdName") String sdName,
                          @Param("wiwName") String wiwName,
                          @Param("emdName") String emdName);

    List<Poll> findBySdName(String sdName);

    List<Poll> findByWiwName(String wiwName);

    List<Poll> findByEmdName(String emdName);
}
