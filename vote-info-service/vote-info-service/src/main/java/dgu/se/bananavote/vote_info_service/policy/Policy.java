package dgu.se.bananavote.vote_info_service.policy;

import jakarta.persistence.*;

@Entity
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // id

    @Column
    private String jdName;  // 정당 이름
    @Column
    private int prmsOrder;  // 정당의 공약 순번
    @Column
    private String prmsTitle; // 공약 제목명
    @Column(length = 3000)
    private String prmsCont; // 공약 내용


    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getJdName() { return jdName; }
    public void setJdName(String jdName) { this.jdName = jdName; }
    public int getPrmsOrder() { return prmsOrder; }
    public void setPrmsOrder(int prmsOrder) { this.prmsOrder = prmsOrder; }
    public String getPrmsTitle() {
        return prmsTitle;
    }
    public void setPrmsTitle(String prmsTitle) {
        this.prmsTitle = prmsTitle;
    }

    public void setPrmsCont(String prmsCont) {
        this.prmsCont = prmsCont;
    }

    @Override
    public String toString() {
        return "Policy{" +
                "id=" + id +
                ", jdName='" + jdName + '\'' +
                ", prmsOrder=" + prmsOrder +
                ", prmsTitle='" + prmsTitle + '\'' +
                ", prmsCont='" + prmsCont + '\'' +
                '}';
    }
}
