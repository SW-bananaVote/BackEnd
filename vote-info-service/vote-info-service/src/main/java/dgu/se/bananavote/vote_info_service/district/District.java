package dgu.se.bananavote.vote_info_service.district;

import jakarta.persistence.*;

@Entity
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  // 구 Id

    @Column
    private String sdName;  // 상위시도명
    @Column
    private String wiwName;  // 구시군명
    @Column
    private String sggName; // 선거구명


    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getSdName() { return sdName; }
    public void setSdName(String sdName) { this.sdName = sdName; }
    public String getWiwName() { return wiwName; }
    public void setWiwName(String wiwName) { this.wiwName = wiwName; }
    public String getSggName() { return sggName; }
    public void setSggName(String sggName) { this.sggName = sggName; }
}
