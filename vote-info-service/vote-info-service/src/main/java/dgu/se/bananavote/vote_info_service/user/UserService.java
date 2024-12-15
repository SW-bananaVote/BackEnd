package dgu.se.bananavote.vote_info_service.user;

import dgu.se.bananavote.vote_info_service.userToken.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserTokenService userTokenService;

    public Optional<User> getUserByUserId(String userId) { return userRepository.findByUserId(userId); }
    public List<Interest> getInterestsByUserId(String userId) { return interestRepository.findByUserId(userId);}
    public Interest addInterest(Interest interest) { return interestRepository.save(interest); }
    public boolean isUserIdExists(String userId) {
        return userRepository.existsById(userId);
    }

    public User registerUser(User user) {
        // 아이디 중복 확인
        if (userRepository.existsById(user.getUserId())) {
            throw new IllegalArgumentException("User ID already exists");
        }

        // 비밀번호 해시 처리
        String hashedPassword = passwordEncoder.encode(user.getHashedPassword());
        user.setHashedPassword(hashedPassword);

        // 유저 저장
        return userRepository.save(user);
    }


    public boolean isPasswordValid(User user, String rawPassword) {
        // 비밀번호 비교 (Spring Security의 PasswordEncoder 사용 권장)
        return passwordEncoder.matches(rawPassword, user.getHashedPassword());
    }

    public Optional<String> findIdByEmail(String email) {
        return userRepository.findByEmail(email).map(User::getUserId);
    }

    public boolean isUserExists(String userId, String email) {
        return userRepository.existsByUserIdAndEmail(userId, email);
    }

    public boolean resetPassword(String userId, String newPassword) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return false;
        }
        user.get().setHashedPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user.get());
        return true;
    }



    public String generateToken(User user) {
        String token = userTokenService.generateToken(user);
        user.setUserToken(userTokenService.findByToken(token).orElseThrow());
        userRepository.save(user);
        return token;
    }

    public String refreshToken(String oldToken) {
        return userTokenService.refreshToken(oldToken);
    }

    public boolean extendTokenExpiration(String token) {
        return userTokenService.extendTokenExpiration(token);
    }


    public void logout(String token) {
        userTokenService.deleteToken(token);
    }


}
