package dgu.se.bananavote.vote_info_service.candidate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CareerRepository careerRepository;

    // 모든 후보자를 가져옵니다.
    public List<Candidate> getCandidate() {
        return candidateRepository.findAll();
    }

    // ID로 후보자를 가져옵니다.
    public Candidate getCandidateById(int id) {
        return candidateRepository.findById(id).orElse(null);
    }

    // 정당 이름으로 후보자를 가져옵니다.
    public List<Candidate> getCandidateByJdName(String jdName) {
        return candidateRepository.findByJdName(jdName);
    }

    // 모든 후보자를 가져옵니다.
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }
    public List<CandidateResponse> getAllCandidatesWithCareers() {
        // 모든 후보자 데이터를 가져오고 경력 정보를 포함한 CandidateResponse로 변환
        return candidateRepository.findAll().stream()
                .map(candidate -> {
                    // 후보자의 경력 가져오기
                    List<Career> careers = careerRepository.findByCnddtId(candidate.getCnddtId());
                    return new CandidateResponse(candidate, careers);
                })
                .collect(Collectors.toList());
    }
    public List<Candidate> filterCandidates(String wiwName, String jdName, String name) {
        // 동적 필터링 로직
        if (wiwName != null && jdName != null && name != null) {
            return candidateRepository.findByWiwNameAndJdNameAndName(wiwName, jdName, name);
        } else if (wiwName != null && jdName != null) {
            return candidateRepository.findByWiwNameAndJdName(wiwName, jdName);
        } else if (wiwName != null && name != null) {
            return candidateRepository.findByWiwNameAndName(wiwName, name);
        } else if (jdName != null && name != null) {
            return candidateRepository.findByJdNameAndName(jdName, name);
        } else if (wiwName != null) {
            return candidateRepository.findByWiwName(wiwName);
        } else if (jdName != null) {
            return candidateRepository.findByJdName(jdName);
        } else if (name != null) {
            return candidateRepository.findByName(name);
        } else {
            return candidateRepository.findAll(); // 조건 없으면 전체 반환
        }
    }

    public boolean existsByCnddtId(String cnddtId) {
        return candidateRepository.existsByCnddtId(cnddtId);
    }

    public Candidate saveCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    public void flush() {
    }
}
