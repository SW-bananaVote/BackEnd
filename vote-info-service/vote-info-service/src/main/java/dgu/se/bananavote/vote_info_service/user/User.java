package dgu.se.bananavote.vote_info_service.user;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.sql.Timestamp;
import java.util.List;

@Entity
public class User {

    @Id
    private String userId;

    private String hashedPassword;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Timestamp createdAt;
    private Timestamp lastLogin;

    // user 엔티티와 매핑, user엔티티가 변동 시 interest엔티티에도 전이됨
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Interest> interests;

    // Getters and Setters
}
