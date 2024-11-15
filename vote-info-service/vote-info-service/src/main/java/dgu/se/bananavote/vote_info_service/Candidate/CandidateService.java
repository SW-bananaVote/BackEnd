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

    public List<Candidate> getCandidate() { return candidateRepository.findAll(); }
    public Candidate getCandidateById(int id) { return candidateRepository.findById(id).orElse(null); }
    public List<Candidate> getCandidateByJdName(String jdName) { return candidateRepository.findByJdName(jdName); }

    public void saveCandidate(Candidate candidate) {
    }
}
