package dgu.se.bananavote.vote_info_service.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    // save(entity)는 JPA에서 자동 제공
    Optional<User> findByUserId(String userId);
    List<User> findAll();
}

