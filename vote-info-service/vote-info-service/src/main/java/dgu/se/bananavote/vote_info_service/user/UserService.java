package dgu.se.bananavote.vote_info_service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InterestRepository interestRepository;

    public Optional<User> getUserByUserId(String userId) { return userRepository.findByUserId(userId); }
    public User registerUser(User user) { return userRepository.save(user); }
    public List<Interest> getInterestsByUserId(String userId) { return interestRepository.findByUserId(userId);}
    public Interest addInterest(Interest interest) { return interestRepository.save(interest); }

}
