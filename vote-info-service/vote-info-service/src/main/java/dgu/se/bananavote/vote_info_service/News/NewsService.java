package dgu.se.bananavote.vote_info_service.News;

//import dgu.se.bananavote.vote_info_service.News.News;
//import dgu.se.bananavote.vote_info_service.News.NewsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class NewsService {

    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public List<News> getNews() {
        // 뉴스탭에서는 uploadDate로만 필터링을 진행함.
        // 따라서 getNews()에서 필터링을 진행할 필요없음
        return newsRepository.findAll();
    }

    public List<News> getNewsByUploadDate(Timestamp uploadDate) {
        return newsRepository.findByUploadDate(uploadDate);
    }

    public Optional<News> getNewsById(Integer id) {
        return newsRepository.findById(id);
    }

    public List<News> getHeadlineNews(Timestamp yesterday) {
        List<News> temp = newsRepository.findByUploadDate(yesterday);
        // 조회수(view) 필드를 기준으로 내림차순 정렬하고, 상위 두 개의 뉴스만 추출
        List<News> headlineList = temp.stream()
                .sorted(Comparator.comparingInt(News::getView).reversed()) // 조회수를 기준으로 내림차순 정렬
                .limit(2) // 상위 두 개의 뉴스만 선택
                .collect(Collectors.toList());

        return headlineList;
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
}