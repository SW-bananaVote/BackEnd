package dgu.se.bananavote.vote_info_service;

import dgu.se.bananavote.vote_info_service.Candidate.CandidateDataUpdater;
import dgu.se.bananavote.vote_info_service.Candidate.PromiseDataUpdater;
import dgu.se.bananavote.vote_info_service.News.NewsCrawler;
import dgu.se.bananavote.vote_info_service.District.DistrictDataUpdater;
import dgu.se.bananavote.vote_info_service.Party.PartyDataUpdater;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class VoteInfoServiceApplication {

	public static void main(String[] args) {
		// Spring Application Context 시작
		ApplicationContext context = SpringApplication.run(VoteInfoServiceApplication.class, args);

		// NewsCrawler 빈을 가져와서 크롤링 메서드 호출
//		testNewsCrawler(context);
		testPartyDataUpdater(context);

		// DistrictDataUpdater 빈을 가져와서 API 데이터 업데이트 테스트 호출
		testDistrictDataUpdater(context);

		//
		testCandidateDataUpdater(context);

		//
		testPromiseDataUpdater(context);
	}

	private static void testNewsCrawler(ApplicationContext context) {
		NewsCrawler newsCrawler = context.getBean(NewsCrawler.class);
		try {
			newsCrawler.crawl();  // 크롤링 테스트 실행
		} catch (Exception e) {
			System.out.println("크롤링 도중 오류 발생: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void testDistrictDataUpdater(ApplicationContext context) {
		DistrictDataUpdater districtDataUpdater = context.getBean(DistrictDataUpdater.class);
		try {
			districtDataUpdater.updateDistrictData();  // API 데이터 업데이트 테스트 실행
			System.out.println("District 데이터 업데이트 완료");
		} catch (Exception e) {
			System.out.println("데이터 업데이트 도중 오류 발생: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void testCandidateDataUpdater(ApplicationContext context) {
		CandidateDataUpdater candidateDataUpdater = context.getBean(CandidateDataUpdater.class);
		try {
			candidateDataUpdater.updateCandidateData();  // Candidate 데이터 업데이트 실행
			System.out.println("Candidate 데이터 업데이트 완료");
		} catch (Exception e) {
			System.out.println("Candidate 데이터 업데이트 도중 오류 발생: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void testPartyDataUpdater(ApplicationContext context) {
		PartyDataUpdater partyDataUpdater = context.getBean(PartyDataUpdater.class);
		try {
			partyDataUpdater.updatePartyData();  // Party 데이터 업데이트 실행
			System.out.println("Party 데이터 업데이트 완료");
		} catch (Exception e) {
			System.out.println("Party 데이터 업데이트 도중 오류 발생: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void testPromiseDataUpdater(ApplicationContext context) {
		PromiseDataUpdater promiseDataUpdater = context.getBean(PromiseDataUpdater.class);
		try {
			promiseDataUpdater.updatePromiseData();  // Promise 데이터 업데이트 실행
			System.out.println("Promise 데이터 업데이트 완료");
		} catch (Exception e) {
			System.out.println("Promise 데이터 업데이트 도중 오류 발생: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
