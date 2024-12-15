package dgu.se.bananavote.vote_info_service.userToken;

import dgu.se.bananavote.vote_info_service.user.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserTokenService {

    @Autowired
    private UserTokenRepository userTokenRepository;

    public String generateToken(User user) {
        String token = UUID.randomUUID().toString();
        UserToken userToken = new UserToken();
        userToken.setUser(user); // User 객체로 대체
        userToken.setToken(token);
        userToken.setExpirationTime(LocalDateTime.now().plusHours(1)); // 1시간 만료

        userTokenRepository.save(userToken);
        return token;
    }

    public boolean validateToken(String token) {
        Optional<UserToken> userToken = userTokenRepository.findByToken(token);
        return userToken.isPresent() && userToken.get().getExpirationTime().isAfter(LocalDateTime.now());
    }

    public void deleteToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token must not be null or empty");
        }

        Optional<UserToken> userToken = userTokenRepository.findByToken(token);
        if (userToken.isEmpty()) {
            throw new IllegalArgumentException("Token not found");
        }

        userTokenRepository.deleteByToken(token);
    }



    public String refreshToken(String oldToken) {
        Optional<UserToken> userTokenOptional = userTokenRepository.findByToken(oldToken);
        if (userTokenOptional.isPresent() && validateToken(oldToken)) {
            UserToken userToken = userTokenOptional.get();
            String newToken = UUID.randomUUID().toString();
            userToken.setToken(newToken);
            userToken.setExpirationTime(LocalDateTime.now().plusHours(1)); // 새로운 만료 시간
            userTokenRepository.save(userToken);
            return newToken;
        }
        throw new IllegalArgumentException("Invalid or expired token");
    }

    @Scheduled(cron = "0 0 * * * *") // 매 시간마다 실행
    public void cleanupExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        List<UserToken> expiredTokens = userTokenRepository.findAll()
                .stream()
                .filter(token -> token.getExpirationTime().isBefore(now))
                .collect(Collectors.toList());
        userTokenRepository.deleteAll(expiredTokens);
    }


    public Optional<UserToken> findByToken(String token) {
        return userTokenRepository.findByToken(token);
    }

    public boolean extendTokenExpiration(String token) {
        Optional<UserToken> userTokenOptional = userTokenRepository.findByToken(token);

        if (userTokenOptional.isPresent()) {
            UserToken userToken = userTokenOptional.get();
            if (userToken.getExpirationTime().isAfter(LocalDateTime.now())) {
                userToken.setExpirationTime(LocalDateTime.now().plusHours(1));
                userTokenRepository.save(userToken);
                return true;
            }
        }
        return false;
    }
}
