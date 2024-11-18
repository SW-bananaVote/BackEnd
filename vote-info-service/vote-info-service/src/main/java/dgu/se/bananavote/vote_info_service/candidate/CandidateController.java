package dgu.se.bananavote.vote_info_service.candidate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @GetMapping
    public List<CandidateResponse> getAllCandidates() {
        // Service를 호출하여 응답 반환
        return candidateService.getAllCandidatesWithCareers();
    }

    //
//    @GetMapping
//    public List<Candidate> getCandidate() { return candidateService.getCandidate(); }
    @GetMapping("/{id}")
    public Candidate getCandidateById(@PathVariable int id) { return candidateService.getCandidateById(id); }
    @GetMapping("/jdName/{jdName}")
    public List<Candidate> getCandidateByJdName(@PathVariable String jdName) { return candidateService.getCandidateByJdName(jdName); }

    @GetMapping("/filter")
    public List<Candidate> filterCandidates(
            @RequestParam(required = false) String wiwName, // 선거구 이름 (optional)
            @RequestParam(required = false) String jdName, // 정당 이름 (optional)
            @RequestParam(required = false) String name    // 후보자 이름 (optional)
    ) {
        // 필터 조건에 맞는 후보자 반환
        return candidateService.filterCandidates(wiwName, jdName, name);
    }
}
