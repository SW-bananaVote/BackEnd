package dgu.se.bananavote.vote_info_service.policy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Integer> {
    List<Policy> findByJdName(String jdName);

    boolean existsByJdNameAndPrmsOrder(String jdName, int prmsOrder);
}
