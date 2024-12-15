package dgu.se.bananavote.vote_info_service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;




    // 유저 등록 기능
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }


    // user엔티티 반환 기능
    @GetMapping("/{userId}")
    public Optional<User> getUser(@PathVariable String userId) {
        return userService.getUserByUserId(userId);
    }
    // 관심사 등록
    @PostMapping("/{userId}/interests")
    public ResponseEntity<Interest> addInterest(@PathVariable String userId, @RequestBody Interest interest) {
        interest.setUserId(userId);  // userId 설정
        Interest savedInterest = userService.addInterest(interest);
        return new ResponseEntity<>(savedInterest, HttpStatus.CREATED);
    }
    // Interest 조회하는 기능(UserId 필요로함)
    @GetMapping("/{userId}/interests")
    public ResponseEntity<List<Interest>> getInterestsByUserId(@PathVariable String userId) {
        List<Interest> interests = userService.getInterestsByUserId(userId);
        // 만약 Interest가 비여있다면 없다고 리턴
        if (interests.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(interests, HttpStatus.OK);
    }

    @PostMapping("/checkId")
    public ResponseEntity<Boolean> checkUserIdExists(@RequestBody UserIdRequest userIdRequest) {
        boolean exists = userService.isUserIdExists(userIdRequest.getUserId());
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> user = userService.getUserByUserId(loginRequest.getUserId());
        if (user.isEmpty() || !userService.isPasswordValid(user.get(), loginRequest.getPassword())) {
            return new ResponseEntity<>("Invalid ID or password", HttpStatus.UNAUTHORIZED);
        }
        String token = userService.generateToken(user.get());
        return new ResponseEntity<>(token, HttpStatus.OK);
    }


    @PostMapping("/findId")
    public ResponseEntity<String> findIdByEmail(@RequestBody EmailRequest emailRequest) {
        Optional<String> userId = userService.findIdByEmail(emailRequest.getEmail());
        return userId.map(id -> new ResponseEntity<>(id, HttpStatus.OK))
                .orElse(new ResponseEntity<>("Email not found", HttpStatus.NOT_FOUND));
    }

    @PostMapping("/isUser")
    public ResponseEntity<Boolean> isUser(@RequestBody UserCheckRequest userCheckRequest) {
        boolean isUser = userService.isUserExists(userCheckRequest.getUserId(), userCheckRequest.getEmail());
        return new ResponseEntity<>(isUser, HttpStatus.OK);
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        boolean updated = userService.resetPassword(resetPasswordRequest.getUserId(), resetPasswordRequest.getNewPassword());
        return updated ? new ResponseEntity<>("Password updated", HttpStatus.OK)
                : new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }



    @PutMapping("/refreshToken")
    public ResponseEntity<String> refreshToken(@RequestBody TokenRequest tokenRequest) {
        try {
            String newToken = userService.refreshToken(tokenRequest.getToken());
            return new ResponseEntity<>(newToken, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping("/extendToken")
    public ResponseEntity<String> extendToken(@RequestBody TokenRequest tokenRequest) {
        boolean extended = userService.extendTokenExpiration(tokenRequest.getToken());
        return extended ? new ResponseEntity<>("Token extended", HttpStatus.OK)
                : new ResponseEntity<>("Invalid or expired token", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody TokenRequest tokenRequest) {
        try {
            userService.logout(tokenRequest.getToken());
            return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }




}

