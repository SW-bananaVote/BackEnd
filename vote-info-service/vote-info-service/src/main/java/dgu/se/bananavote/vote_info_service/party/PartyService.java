package dgu.se.bananavote.vote_info_service.party;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartyService {

    private final PartyRepository partyRepository;

    @Autowired
    public PartyService(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    public Party saveParty(Party party) {
        return partyRepository.save(party);
    }

    // 모든 Party 데이터를 반환하는 메서드 추가
    public List<Party> getAllParties() {
        return partyRepository.findAll();
    }
}
