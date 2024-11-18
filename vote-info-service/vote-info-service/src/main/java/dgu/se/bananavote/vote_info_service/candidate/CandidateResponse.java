package dgu.se.bananavote.vote_info_service.candidate;

import java.util.List;

public class CandidateResponse {
    private int id;
    private String cnddtId;
    private String sgId;
    private String jdName;
    private String wiwName;
    private String name;
    private String career1; // 첫 번째 경력
    private String career2; // 두 번째 경력

    // Constructor
    public CandidateResponse(Candidate candidate, List<Career> careers) {
        this.id = candidate.getId();
        this.cnddtId = candidate.getCnddtId();
        this.sgId = candidate.getSgId();
        this.jdName = candidate.getJdName();
        this.wiwName = candidate.getWiwName();
        this.name = candidate.getName();

        // 경력 데이터 설정
        this.career1 = careers.size() > 0 ? careers.get(0).getCareer() : null;
        this.career2 = careers.size() > 1 ? careers.get(1).getCareer() : null;
    }

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

    public String getCareer1() {
        return career1;
    }

    public void setCareer1(String career1) {
        this.career1 = career1;
    }

    public String getCareer2() {
        return career2;
    }

    public void setCareer2(String career2) {
        this.career2 = career2;
    }
}
