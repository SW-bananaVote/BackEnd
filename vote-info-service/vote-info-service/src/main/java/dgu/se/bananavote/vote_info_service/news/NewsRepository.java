package dgu.se.bananavote.vote_info_service.news;

//import dgu.se.bananavote.vote_info_service.News.News;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
    // 기본 제공 CURD 메서드. 없어도 제공하기 때문에 굳이 선언할 필요 없음.
    //Optional<News> findById(Integer id);
    //List<News> findAll();

    List<News> findByUploadDate(Timestamp uploadDate);
    boolean existsByTitleAndUploadDate(String title, Timestamp uploadDate);
    boolean existsByTitle(String title);

    List<News> findAllByUploadDateBetween(Timestamp startDate, Timestamp endDate);
}
