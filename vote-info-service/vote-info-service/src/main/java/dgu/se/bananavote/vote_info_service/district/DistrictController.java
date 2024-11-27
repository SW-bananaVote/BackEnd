package dgu.se.bananavote.vote_info_service.district;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/district")
public class DistrictController {

    private final DistrictService districtService;

    @Autowired
    public DistrictController(DistrictService districtService) { this.districtService = districtService; }

    @GetMapping
    public List<District> getDistrict() { return districtService.getDistrict(); }
    @GetMapping("/wiwName/{wiwName}")
    public List<District> getDistrictByWiwName(@PathVariable String wiwName) { return districtService.getDistrictByWiwName(wiwName); }
    @GetMapping("/sdName/{sdName}")
    public List<District> getDistrictBySdName(@PathVariable String sdName) { return districtService.getDistrictBySdName(sdName); }
}