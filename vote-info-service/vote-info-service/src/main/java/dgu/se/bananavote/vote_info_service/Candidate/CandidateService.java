package dgu.se.bananavote.vote_info_service.Candidate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private PromiseRepository promiseRepository;
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

    // 후보자를 저장합니다.
    public void saveCandidate(Candidate candidate) {
        candidateRepository.save(candidate);
    }

    // 모든 후보자를 가져옵니다.
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }
}
