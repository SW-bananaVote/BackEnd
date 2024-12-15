package dgu.se.bananavote.vote_info_service.user;

import dgu.se.bananavote.vote_info_service.userToken.UserToken;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {

    @Id
    private String userId;
    private String hashedPassword;
    private String nickname;
    private String email;

    // user 엔티티와 매핑, user엔티티가 변동 시 interest엔티티에도 전이됨
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Interest> interests;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserToken userToken;

    public String getUserId() {
        return userId;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }

    public UserToken getUserToken() {
        return userToken;
    }

    public void setUserToken(UserToken userToken) {
        this.userToken = userToken;
    }
}
