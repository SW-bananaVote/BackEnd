package dgu.se.bananavote.vote_info_service.info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InfoService {

    private final InfoRepository infoRepository;

    @Autowired
    public InfoService(InfoRepository infoRepository) { this.infoRepository = infoRepository; }

    public List<Info> getInfo() { return infoRepository.findAll(); }

    public Optional<Info> getInfoById(int id) { return infoRepository.findById(id); }
}
