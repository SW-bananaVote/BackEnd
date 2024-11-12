package dgu.se.bananavote.vote_info_service.News;

//import dgu.se.bananavote.vote_info_service.News.News;
//import dgu.se.bananavote.vote_info_service.News.NewsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.sql.Timestamp;
import java.util.Calendar;

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




