package dgu.se.bananavote.vote_info_service.candidate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @GetMapping
    public List<CandidateResponse> getCandidatePartly() {
        // 조회할 정당 이름 목록 정의
        List<String> partyNames = List.of("더불어민주당", "국민의힘", "녹색정의당");

        // Service 호출 및 결과 반환
        return candidateService.getCandidatesByPartyNames(partyNames);
    }

    @GetMapping("all")
    public List<CandidateResponse> getAllCandidates() {
        return candidateService.getAllCandidatesWithCareers();
    }

    @GetMapping("/{id}")
    public CandidateResponse getCandidateById(@PathVariable int id) {
        Candidate candidate = candidateService.getCandidateById(id);
        if (candidate == null) {
            throw new IllegalArgumentException("Candidate not found with ID: " + id);
        }
        List<Career> careers = candidateService.getCareersByCnddtId(candidate.getCnddtId());
        return new CandidateResponse(candidate, careers);
    }

    @GetMapping("/jdName/{jdName}")
    public List<CandidateResponse> getCandidateByJdName(@PathVariable String jdName) {
        List<Candidate> candidates = candidateService.getCandidateByJdName(jdName);
        return candidates.stream()
                .map(candidate -> {
                    List<Career> careers = candidateService.getCareersByCnddtId(candidate.getCnddtId());
                    return new CandidateResponse(candidate, careers);
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/filter")
    public List<CandidateResponse> filterCandidates(
            @RequestParam(required = false) String wiwName,
            @RequestParam(required = false) String jdName,
            @RequestParam(required = false) String name) {
        List<Candidate> candidates = candidateService.filterCandidates(wiwName, jdName, name);
        return candidates.stream()
                .map(candidate -> {
                    List<Career> careers = candidateService.getCareersByCnddtId(candidate.getCnddtId());
                    return new CandidateResponse(candidate, careers);
                })
                .collect(Collectors.toList());
    }

}
