package dgu.se.bananavote.vote_info_service.poll;

import jakarta.persistence.*;

@Entity
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sgId; // 선거 ID

    @Column(nullable = false)
    private String psName; // 투표소 이름

    @Column(nullable = false)
    private String sdName; // 시도 이름

    @Column(nullable = false)
    private String wiwName; // 구 이름

    @Column(nullable = false)
    private String emdName; // 동 이름

    @Column(nullable = false)
    private String placeName; // 장소 이름

    @Column(nullable = false)
    private String addr; // 주소

    @Column
    private String floor; // 층 정보

    // Getters and Setters
    // (생략 가능: Lombok 사용 추천 @Getter, @Setter)


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSgId() {
        return sgId;
    }

    public void setSgId(String sgId) {
        this.sgId = sgId;
    }

    public String getPsName() {
        return psName;
    }

    public void setPsName(String psName) {
        this.psName = psName;
    }

    public String getSdName() {
        return sdName;
    }

    public void setSdName(String sdName) {
        this.sdName = sdName;
    }

    public String getWiwName() {
        return wiwName;
    }

    public void setWiwName(String wiwName) {
        this.wiwName = wiwName;
    }

    public String getEmdName() {
        return emdName;
    }

    public void setEmdName(String emdName) {
        this.emdName = emdName;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }
}
