package dgu.se.bananavote.vote_info_service.Policy;

import jakarta.persistence.*;

@Entity
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String jdName;  // 정당 이름
    @Column
    private int prmsOrder;  // 정책 번호
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getJdName() { return jdName; }
    public void setJdName(String jdName) { this.jdName = jdName; }
    public int getPrmsOrder() { return prmsOrder; }
    public void setPrmsOrder(int prmsOrder) { this.prmsOrder = prmsOrder; }
}
