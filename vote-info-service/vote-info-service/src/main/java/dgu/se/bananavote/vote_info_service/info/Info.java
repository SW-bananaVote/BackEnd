package dgu.se.bananavote.vote_info_service.info;

import jakarta.persistence.*;


@Entity
public class Info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  // Info ID

    @Column(name = "title", nullable = false)
    private String title;  // Info title

    //기본 생성자
    public Info() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
