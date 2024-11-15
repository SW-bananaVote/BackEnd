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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCnddtId() {
        return cnddtId;
    }

    public void setCnddtId(String cnddtId) {
        this.cnddtId = cnddtId;
    }

    public int getCareerOrder() {
        return careerOrder;
    }

    public void setCareerOrder(int careerOrder) {
        this.careerOrder = careerOrder;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }
}
