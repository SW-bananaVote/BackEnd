package dgu.se.bananavote.vote_info_service.news;

//import dgu.se.bananavote.vote_info_service.News.News;
//import dgu.se.bananavote.vote_info_service.News.NewsRepository;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewsService {

    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public Timestamp convertStringToTimestamp(String dateString) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME);
        return Timestamp.valueOf(localDateTime);
    }


    public List<News> getNews() {
        // 뉴스탭에서는 uploadDate로만 필터링을 진행함.
        // 따라서 getNews()에서 필터링을 진행할 필요없음
        return newsRepository.findAll();
    }

    public Optional<News> getNewsById(Integer id) {
        return newsRepository.findById(id);
    }



    public List<News> getHeadlineNews(Timestamp yesterday) {
        // 어제의 시작 시간과 종료 시간 계산
        LocalDate localDate = yesterday.toLocalDateTime().toLocalDate();
        Timestamp startOfDay = Timestamp.valueOf(localDate.atStartOfDay());
        Timestamp endOfDay = Timestamp.valueOf(localDate.atTime(LocalTime.MAX));

        // 어제 날짜의 뉴스 데이터를 범위로 조회
        List<News> temp = newsRepository.findAllByUploadDateBetween(startOfDay, endOfDay);

        // 조회수(view) 필드를 기준으로 내림차순 정렬하고, 상위 두 개의 뉴스만 추출
        return temp.stream()
                .sorted(Comparator.comparingInt(News::getView).reversed())
                .limit(2)
                .collect(Collectors.toList());
    }


    public News saveNews(News news) {
        return newsRepository.save(news);
    }

//    public void deleteNewsById(Integer id) {
//        newsRepository.deleteById(id);
//    }

    public boolean existsByTitleAndUploadDate(String title, Timestamp uploadDate) {
        return newsRepository.existsByTitleAndUploadDate(title, uploadDate);
    }

    public boolean existsByTitle(String title) {
        return newsRepository.existsByTitle(title);
    }

    public List<News> getNewsByUploadDateRange(Timestamp startDateTime, Timestamp endDateTime) {
        return newsRepository.findAllByUploadDateBetween(startDateTime, endDateTime);
    }

}