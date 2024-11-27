package dgu.se.bananavote.vote_info_service.poll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PollService {

    private final PollRepository pollRepository;

    @Autowired
    public PollService(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    public Poll savePoll(Poll poll) {
        return pollRepository.save(poll);
    }

    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
    }

    public List<Poll> getPollsBySdName(String sdName) {
        return pollRepository.findBySdName(sdName);
    }

    public List<Poll> getPollsByWiwName(String wiwName) {
        return pollRepository.findByWiwName(wiwName);
    }

    public List<Poll> getPollsByEmdName(String emdName) { return pollRepository.findByEmdName(emdName);}

    public boolean existsBySgIdAndPsNameAndAddr(String sgId, String psName, String addr) {
        return pollRepository.existsBySgIdAndPsNameAndAddr(sgId, psName, addr);
    }

    public List<Poll> filterPoll(String sdName, String wiwName, String emdName) {
        return pollRepository.filterPoll(sdName, wiwName, emdName);
    }
}
