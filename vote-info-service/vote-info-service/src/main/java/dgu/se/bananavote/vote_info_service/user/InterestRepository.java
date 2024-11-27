package dgu.se.bananavote.vote_info_service.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Integer> {
    List<Interest> findByUserId(String userId);
    List<Interest> findAll();
}
