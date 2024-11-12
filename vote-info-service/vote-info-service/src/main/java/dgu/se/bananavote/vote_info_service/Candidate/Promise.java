package dgu.se.bananavote.vote_info_service.Candidate;

import jakarta.persistence.*;

@Entity
public class Promise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  // 공약 Id

    @Column
    private String cnddtId;  // 후보자 Id
    @Column
    private int promiseOrder;  // 공약 번호
    @Column
    private String promiseTitle;  // 공약 명
    @Column
    private String promiseContent;  // 공약 내용
    // Getters and Setters
}
