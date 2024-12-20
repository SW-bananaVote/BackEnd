package dgu.se.bananavote.vote_info_service.news;

//import dgu.se.bananavote.vote_info_service.News.News;
//import dgu.se.bananavote.vote_info_service.News.NewsService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public List<News> getNews() {
        return newsService.getNews();
    }

    @GetMapping("/{id}")
    public Optional<News> getNewsById(@PathVariable Integer id) {
        return newsService.getNewsById(id);
    }

    @GetMapping("/date/{date}")
    public List<News> getNewsByUploadDate(@PathVariable String date) {
        try {
            // 입력된 날짜를 LocalDate로 변환
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // 시작 시간과 종료 시간 생성 (UTC 기준)
            ZonedDateTime startOfDay = localDate.atStartOfDay(ZoneId.of("UTC"));
            ZonedDateTime endOfDay = localDate.atTime(LocalTime.MAX).atZone(ZoneId.of("UTC"));

            // ZonedDateTime -> Timestamp 변환
            Timestamp startTimestamp = Timestamp.from(startOfDay.toInstant());
            Timestamp endTimestamp = Timestamp.from(endOfDay.toInstant());

            // 서비스 호출
            return newsService.getNewsByUploadDateRange(startTimestamp, endTimestamp);
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format. Use 'yyyy-MM-dd'");
        }
    }




    @GetMapping("/headline")
    public List<News> getHeadlineNews() {
        // 어제 날짜를 구함
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);  // 하루 전으로 설정
        Timestamp yesterday = new Timestamp(calendar.getTimeInMillis());

        // 어제 날짜 뉴스 중 조회수가 가장 높은 뉴스를 가져옴.
        return newsService.getHeadlineNews(yesterday);
    }

}




