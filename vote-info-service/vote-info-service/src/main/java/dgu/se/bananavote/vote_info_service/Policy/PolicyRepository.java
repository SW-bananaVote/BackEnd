package dgu.se.bananavote.vote_info_service.Policy;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyRepository extends JpaRepository<Policy, Integer> {
    List<Policy> findByJdName(String jdName);
}
