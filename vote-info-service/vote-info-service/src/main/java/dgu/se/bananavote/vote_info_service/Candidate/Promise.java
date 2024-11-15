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

    public int getPromiseOrder() {
        return promiseOrder;
    }

    public void setPromiseOrder(int promiseOrder) {
        this.promiseOrder = promiseOrder;
    }

    public String getPromiseTitle() {
        return promiseTitle;
    }

    public void setPromiseTitle(String promiseTitle) {
        this.promiseTitle = promiseTitle;
    }

    public String getPromiseContent() {
        return promiseContent;
    }

    public void setPromiseContent(String promiseContent) {
        this.promiseContent = promiseContent;
    }
}
