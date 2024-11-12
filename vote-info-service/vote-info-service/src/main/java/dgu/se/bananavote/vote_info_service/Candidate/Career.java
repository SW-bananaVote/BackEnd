package dgu.se.bananavote.vote_info_service.Candidate;

import jakarta.persistence.*;

@Entity
public class Career {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  // 경력 Id

    @Column
    private String cnddtId;  // 후보자 Id
    @Column
    private int careerOrder;  // 경력 번호
    @Column
    private String career;  // 경력 내용
    // Getters and Setters
}
