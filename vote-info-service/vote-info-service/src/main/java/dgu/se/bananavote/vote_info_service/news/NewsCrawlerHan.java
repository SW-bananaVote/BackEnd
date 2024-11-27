package dgu.se.bananavote.vote_info_service.news;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class NewsCrawlerHan {
    private final NewsService newsService;

    public NewsCrawlerHan(NewsService newsService) {
        this.newsService = newsService;
    }

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void crawlAndSaveNews() {
        int pageLimit = 10;
        String baseUrl = "https://www.hani.co.kr/arti/politics?page=";

        for (int page = 1; page <= pageLimit; page++) {
            try {
                String url = baseUrl + page;
                Document doc = Jsoup.connect(url).get();

                // Step 1: Extract JSON from the <script> tag
                Element scriptTag = doc.select("script[type=application/json]").first();
                if (scriptTag == null) {
                    System.out.println("No JSON script tag found on page: " + page);
                    continue;
                }

                String jsonData = scriptTag.html();
//                System.out.println("Extracted JSON: " + jsonData); // For debugging

                // Step 2: Parse the nested JSON structure
                JsonObject rootObject = JsonParser.parseString(jsonData).getAsJsonObject();
                JsonObject props = rootObject.getAsJsonObject("props");
                if (props == null) {
                    System.out.println("No 'props' found in JSON for page: " + page);
                    continue;
                }

                JsonObject pageProps = props.getAsJsonObject("pageProps");
                if (pageProps == null) {
                    System.out.println("No 'pageProps' found in JSON for page: " + page);
                    continue;
                }

                JsonObject listData = pageProps.getAsJsonObject("listData");
                if (listData == null) {
                    System.out.println("No 'listData' found in JSON for page: " + page);
                    continue;
                }

                JsonArray articleList = listData.getAsJsonArray("articleList");
                if (articleList == null || articleList.size() == 0) {
                    System.out.println("No 'articleList' found or it is empty for page: " + page);
                    continue;
                }

                // Step 3: Process each article
                for (int i = 0; i < articleList.size(); i++) {
                    JsonObject article = articleList.get(i).getAsJsonObject();

                    int id = article.get("id").getAsInt();
                    String title = article.get("title").getAsString();
                    String articleUrl = "https://www.hani.co.kr" + article.get("url").getAsString();
                    String imgUrl = article.get("image").getAsString();

                    // Handle empty reporterList
                    String reporterName = "Unknown"; // Default value if no reporter
                    JsonArray reporterList = article.getAsJsonArray("reporterList");
                    if (reporterList != null && reporterList.size() > 0) {
                        reporterName = reporterList.get(0).getAsJsonObject().get("name").getAsString();
                    }

                    String uploadDateStr = article.get("updateDate").getAsString();
                    Timestamp uploadDate = parseTimestamp(uploadDateStr);

                    // Avoid duplicates
                    if (!newsService.existsByTitleAndUploadDate(title, uploadDate)) {
                        News news = new News();
                        news.setId(id);
                        news.setUrl(articleUrl);
                        news.setTitle(title);
                        news.setImgUrl(imgUrl);
                        news.setAuthor(reporterName);
                        news.setUploadDate(uploadDate);

                        // Fetch detailed content for each article
                        String content = fetchContent(articleUrl);
                        news.setContent(content);

                        newsService.saveNews(news);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Error processing page " + page + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private String fetchContent(String articleUrl) {
        try {
            Document articleDoc = Jsoup.connect(articleUrl).get();

            // Extract JSON data from the detailed article page
            Element scriptTag = articleDoc.select("script[type=application/json]").first();
            if (scriptTag == null) {
                System.out.println("No JSON script tag found on article page: " + articleUrl);
                return "";
            }

            String jsonData = scriptTag.html();
            JsonObject rootObject = JsonParser.parseString(jsonData).getAsJsonObject();

            // Navigate to content inside props -> pageProps -> article -> content
            JsonObject props = rootObject.getAsJsonObject("props");
            if (props == null) {
                System.out.println("No 'props' found in JSON for article: " + articleUrl);
                return "";
            }

            JsonObject pageProps = props.getAsJsonObject("pageProps");
            if (pageProps == null) {
                System.out.println("No 'pageProps' found in JSON for article: " + articleUrl);
                return "";
            }

            JsonObject article = pageProps.getAsJsonObject("article");
            if (article == null) {
                System.out.println("No 'article' found in JSON for article: " + articleUrl);
                return "";
            }

            String content = article.get("content").getAsString();
            System.out.println("컨텐츠 출력");
            System.out.println(content);
            return content;

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } catch (Exception e) {
            System.err.println("Error fetching content from article: " + articleUrl);
            e.printStackTrace();
            return "";
        }
    }


    private Timestamp parseTimestamp(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return Timestamp.valueOf(LocalDateTime.parse(dateStr, formatter));
    }
}
