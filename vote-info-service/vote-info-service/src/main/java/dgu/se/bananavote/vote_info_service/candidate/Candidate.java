package dgu.se.bananavote.vote_info_service.candidate;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  // 후보자 클래스 Id

    @Column
    private String cnddtId;  // 후보자 Id
    @Column
    private String sgId;  // 선거 Id
    @Column
    private String jdName;  // 정당 이름
    @Column
    private String wiwName;  // 구시군명
    @Column
    private String name;  // 후보자 이름
    // Getters and Setters

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Career> careers;

    // Getters and Setters
    public List<Career> getCareers() {
        return careers;
    }

    public void setCareers(List<Career> careers) {
        this.careers = careers;
    }



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

    public String getSgId() {
        return sgId;
    }

    public void setSgId(String sgId) {
        this.sgId = sgId;
    }

    public String getJdName() {
        return jdName;
    }

    public void setJdName(String jdName) {
        this.jdName = jdName;
    }

    public String getWiwName() {
        return wiwName;
    }

    public void setWiwName(String wiwName) {
        this.wiwName = wiwName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
