package dgu.se.bananavote.vote_info_service.news;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  // 뉴스 ID

    @Column(nullable = false)//
    private String url;  // 크롤링한 뉴스 URL

    @Column(nullable = false, length = 500)//
    private String title;  // 뉴스 제목

    @Column(nullable = false, columnDefinition = "TEXT")//
    private String content;  // 뉴스 내용

    @Column(name = "upload_date", nullable = false)
    private Timestamp uploadDate;  // 뉴스 업로드 날짜

//    @Column(name = "modify_date", nullable = false)
//    private Timestamp modifyDate;  // 뉴스 수정 날짜

    @Column(length = 255)//
    private String author;  // 이름

    @Column(name = "img_url", columnDefinition = "TEXT")//
    private String imgUrl;  // 뉴스 썸네일 이미지 URL

    private int view;  // 조회수

    // 기본 생성자
    public News() {}

    // Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Timestamp getUploadDate() { return uploadDate; }
    public void setUploadDate(Timestamp uploadDate) { this.uploadDate = uploadDate; }
//
//    public Timestamp getModifyDate() { return modifyDate; }
//    public void setModifyDate(Timestamp modifyDate) { this.modifyDate = modifyDate; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getImgUrl() { return imgUrl; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }

    public int getView() { return view; }
    public void setView(int view) { this.view = view; }
}
