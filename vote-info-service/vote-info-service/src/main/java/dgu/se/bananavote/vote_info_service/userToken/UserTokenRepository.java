package dgu.se.bananavote.vote_info_service.userToken;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserToken ut WHERE ut.token = :token")
    void deleteByToken(@Param("token") String token);

}

