package dgu.se.bananavote.vote_info_service.user;

import jakarta.persistence.*;

@Entity
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String userId;
    private String name;

    // user엔티티의 userId를 참조가능, INSERT와UPDATE는 불가
    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    // Getters and Setters 적는 칸
    public void setUserId(String userId) { this.userId = userId; }
}
