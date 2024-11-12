//package dgu.se.bananavote.vote_info_service.News;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//
//@Service
//public class NewsCrawler {
//
//    private final NewsService newsService;
//
//    @Autowired
//    public NewsCrawler(NewsService newsService) {
//        this.newsService = newsService;
//    }
//
//    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
//    public void crawl() {
//        int page = 1;
//        boolean hasMoreNews = true;
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR, -3); // 3일 전 설정
//        Timestamp threeDaysAgo = new Timestamp(calendar.getTimeInMillis());
//        int threeDaysAgoInt = Integer.parseInt(dateFormat.format(threeDaysAgo.getTime()));
//        calendar.add(Calendar.DAY_OF_YEAR, 3); // 오늘로 복원
//        int regDate = Integer.parseInt(dateFormat.format(calendar.getTime()));
//
//        int existCount = 0;
//
//        while (hasMoreNews) {
//            try {
//                String url = "https://news.daum.net/breakingnews/politics/president?page=" + page + "&regDate=" + regDate;
//                Thread.sleep(1000); // 요청 속도 조절
//                Document document = Jsoup.connect(url).get();
//                Elements newsElements = document.select("div.cont_thumb");
//
//                if (regDate <= threeDaysAgoInt) {
//                    System.out.println("3일 전 날짜에 도달하여 크롤링 중단");
//                    break;
//                }
//
//                for (Element newsElement : newsElements) {
//                    String newsUrl = newsElement.select("a.link_txt").attr("abs:href");
//
//                    if (newsUrl.contains("/series/")) {
//                        continue;
//                    }
//
//                    Document newsDoc = Jsoup.connect(newsUrl).get();
//                    String title = newsDoc.select("title").text();
//
//                    if (newsService.existsByTitle(title)) {
//                        existCount++;
//                        if (existCount >= 30) {
//                            hasMoreNews = false;
//                            break;
//                        }
//                        continue;
//                    }
//
//                    String author = newsDoc.select("span.txt_info").first() != null ?
//                            newsDoc.select("span.txt_info").first().text() : "Unknown Author";
//                    String uploadDateStr = newsDoc.select("span.num_date").first() != null ?
//                            newsDoc.select("span.num_date").first().text() : "";
//                    Timestamp uploadDate = uploadDateStr.isEmpty() ? new Timestamp(System.currentTimeMillis()) :
//                            parseDate(uploadDateStr);
//                    String imgUrl = newsDoc.select("meta[property=og:image]").attr("content");
//
//                    if (uploadDate.before(threeDaysAgo)) {
//                        hasMoreNews = false;
//                        break;
//                    }
//
//                    Elements paragraphElements = newsDoc.select("p[dmcf-ptype=general]");
//                    StringBuilder contentBuilder = new StringBuilder();
//                    for (Element paragraph : paragraphElements) {
//                        contentBuilder.append(paragraph.text()).append(" ");
//                    }
//                    String content = contentBuilder.toString().trim();
//
//                    if (!newsService.existsByTitleAndUploadDate(title, uploadDate)) {
//                        News news = new News();
//                        news.setTitle(title);
//                        news.setUrl(newsUrl);
//                        news.setImgUrl(imgUrl);
//                        news.setContent(content);
//                        news.setAuthor(author);
//                        news.setUploadDate(uploadDate);
//                        news.setView(0);
//                        newsService.saveNews(news);
//                        System.out.println("저장된 뉴스: " + news.getTitle() + news.getUploadDate());
//                    } else {
//                        System.out.println("중복된 뉴스: " + title + uploadDate);
//                        existCount++;
//                    }
//                }
//
//                if (newsElements.size()-4 == existCount) {
//                    calendar.add(Calendar.DAY_OF_YEAR, -1);
//                    regDate = Integer.parseInt(dateFormat.format(calendar.getTime()));
//                    page = 1;
//                    existCount = 0;
//                } else {
//                    page++;
//                    System.out.println(existCount);
//                    existCount = 0;
//                }
//
//            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
//                break;
//            }
//        }
//    }
//
//
//    // 날짜 문자열을 Timestamp로 변환하는 메서드
//    private Timestamp parseDate(String dateStr) {
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd. HH:mm");
//            return new Timestamp(dateFormat.parse(dateStr).getTime());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new Timestamp(Calendar.getInstance().getTimeInMillis());
//        }
//    }
//}



package dgu.se.bananavote.vote_info_service.News;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
public class NewsCrawler {

    private static final Logger logger = LoggerFactory.getLogger(NewsCrawler.class); // Logger 설정

    private final NewsService newsService;

    @Autowired
    public NewsCrawler(NewsService newsService) {
        this.newsService = newsService;
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void crawl() {
        int page = 1;
        boolean hasMoreNews = true;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -3); // 3일 전 설정
        Timestamp threeDaysAgo = new Timestamp(calendar.getTimeInMillis());
        int threeDaysAgoInt = Integer.parseInt(dateFormat.format(threeDaysAgo.getTime()));
        calendar.add(Calendar.DAY_OF_YEAR, 3); // 오늘로 복원
        int regDate = Integer.parseInt(dateFormat.format(calendar.getTime()));

        int existCount = 0;

        while (hasMoreNews) {
            try {
                String url = "https://news.daum.net/breakingnews/politics/president?page=" + page + "&regDate=" + regDate;
                Thread.sleep(1000); // 요청 속도 조절
                Document document = Jsoup.connect(url).get();
                Elements newsElements = document.select("div.cont_thumb");

                if (regDate <= threeDaysAgoInt) {
                    logger.info("3일 전 날짜에 도달하여 크롤링 중단");
                    break;
                }

                for (Element newsElement : newsElements) {
                    String newsUrl = newsElement.select("a.link_txt").attr("abs:href");

                    if (newsUrl.contains("/series/")) {
                        continue;
                    }

                    Document newsDoc = Jsoup.connect(newsUrl).get();
                    String title = newsDoc.select("title").text();

                    if(newsService.existsByTitle(title)) {
                        existCount++;
                        if (existCount >= 30) {
                            hasMoreNews = false;
                            break;
                        }
                        continue;
                    }

                    String author = newsDoc.select("span.txt_info").first() != null ?
                            newsDoc.select("span.txt_info").first().text() : "Unknown Author";
                    String uploadDateStr = newsDoc.select("span.num_date").first() != null ?
                            newsDoc.select("span.num_date").first().text() : "";
                    Timestamp uploadDate = uploadDateStr.isEmpty() ? new Timestamp(System.currentTimeMillis()) :
                            parseDate(uploadDateStr);
                    String imgUrl = newsDoc.select("meta[property=og:image]").attr("content");

                    if (uploadDate.before(threeDaysAgo)) {
                        hasMoreNews = false;
                        logger.info("중단: 3일 이전 뉴스 발견");
                        break;
                    }

                    Elements paragraphElements = newsDoc.select("p[dmcf-ptype=general]");
                    StringBuilder contentBuilder = new StringBuilder();
                    for (Element paragraph : paragraphElements) {
                        contentBuilder.append(paragraph.text()).append(" ");
                    }
                    String content = contentBuilder.toString().trim();

                    if (!newsService.existsByTitleAndUploadDate(title, uploadDate)) {
                        News news = new News();
                        news.setTitle(title);
                        news.setUrl(newsUrl);
                        news.setImgUrl(imgUrl);
                        news.setContent(content);
                        news.setAuthor(author);
                        news.setUploadDate(uploadDate);
                        news.setView(0);
                        newsService.saveNews(news);
                        logger.info("저장된 뉴스: {} {}", news.getTitle(), news.getUploadDate());
                    } else {
                        logger.info("중복된 뉴스: {} {}", title, uploadDate);
                        existCount++;
                    }
                }

                if (newsElements.size() - 4 == existCount) {
                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                    regDate = Integer.parseInt(dateFormat.format(calendar.getTime()));
                    page = 1;
                    existCount = 0;
                } else {
                    page++;
                    logger.info("다음 페이지로 이동: {}", page);
                    existCount = 0;
                }

            } catch (IOException | InterruptedException e) {
                logger.error("크롤링 중 오류 발생", e);
                break;
            }
        }
    }

    private Timestamp parseDate(String dateStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd. HH:mm");
            return new Timestamp(dateFormat.parse(dateStr).getTime());
        } catch (Exception e) {
            logger.error("날짜 파싱 오류", e);
            return new Timestamp(Calendar.getInstance().getTimeInMillis());
        }
    }
}
