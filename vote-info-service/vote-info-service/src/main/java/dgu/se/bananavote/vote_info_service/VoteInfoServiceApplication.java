package dgu.se.bananavote.vote_info_service;

import dgu.se.bananavote.vote_info_service.chatgpt.ChatController;
import dgu.se.bananavote.vote_info_service.district.DistrictDataUpdater;
import dgu.se.bananavote.vote_info_service.news.NewsCrawlerHan;
import dgu.se.bananavote.vote_info_service.party.PartyDataUpdater;
import dgu.se.bananavote.vote_info_service.candidate.CandidateDataUpdater;
import dgu.se.bananavote.vote_info_service.policy.PolicyDataUpdater;
import dgu.se.bananavote.vote_info_service.poll.PollDataUpdater;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Map;

@SpringBootApplication
@EnableScheduling
public class VoteInfoServiceApplication {

	public static void main(String[] args) {
		System.out.println(System.getenv("SPRING_SECURITY_USER_PASSWORD"));

		SpringApplication.run(VoteInfoServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(ApplicationContext context) {
		return args -> {
			// Get the DistrictDataUpdater bean from the Spring context
			PartyDataUpdater partyDataUpdater = context.getBean(PartyDataUpdater.class);
			PolicyDataUpdater policyDataUpdater = context.getBean(PolicyDataUpdater.class);
			DistrictDataUpdater districtDataUpdater = context.getBean(DistrictDataUpdater.class);
			PollDataUpdater pollDataUpdater = context.getBean(PollDataUpdater.class);
			CandidateDataUpdater candidateDataUpdater = context.getBean(CandidateDataUpdater.class);
			NewsCrawlerHan newsCrawler = context.getBean(NewsCrawlerHan.class);


//			// Execute the updateDistrictData method
//			partyDataUpdater.updatePartyData();
//			policyDataUpdater.updatePolicyData();
//			districtDataUpdater.updateDistrictData();
//			pollDataUpdater.updatePollData();
//			candidateDataUpdater.updateCandidateData();
//			newsCrawler.crawlAndSaveNews();

			try {
				// 테스트 요청 생성
				Map<String, String> testRequest = Map.of("message", "What is the capital of France?");

				// ChatController의 chat 메서드 호출
				ChatController chatController = context.getBean(ChatController.class);
				Map<String, Object> response = chatController.chat(testRequest);

				// 결과 출력
				System.out.println("ChatController 테스트 성공:");
				System.out.println(response);
			} catch (Exception e) {
				// 에러 처리
				System.out.println("ChatController 테스트 중 오류 발생: " + e.getMessage());
				e.printStackTrace();
			}

		};
	}
}
