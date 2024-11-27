package dgu.se.bananavote.vote_info_service.info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/info")
public class InfoController {

    private final InfoService infoService;

    @Autowired
    public InfoController(InfoService infoService) { this.infoService = infoService; }

    @GetMapping
    public List<Info> getInfo() { return infoService.getInfo(); }

    @GetMapping("/{id}")
    public Optional<Info> getInfoById(@PathVariable int id) { return infoService.getInfoById(id); }
}
