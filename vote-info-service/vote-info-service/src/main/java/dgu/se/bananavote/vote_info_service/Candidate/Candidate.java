package dgu.se.bananavote.vote_info_service.Candidate;


import jakarta.persistence.*;

@Entity
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  // 후보자 클래스 Id

    @Column
    private String cnddtId;  // 후보자 Id
    @Column
    private String sgjdId;  // 선거정당 Id
    @Column
    private String jdName;  // 정당 이름
    @Column
    private String wiwName;  // 구시군명
    @Column
    private String name;  // 후보자 이름
    // Getters and Setters
}
