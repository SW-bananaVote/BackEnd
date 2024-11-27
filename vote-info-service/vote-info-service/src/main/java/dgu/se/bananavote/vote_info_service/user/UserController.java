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
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }
//    @GetMapping("login")
//    public ResponseEntity<User> login(@RequestParam String nickname, @RequestParam String password) {
//
//    }
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
}

