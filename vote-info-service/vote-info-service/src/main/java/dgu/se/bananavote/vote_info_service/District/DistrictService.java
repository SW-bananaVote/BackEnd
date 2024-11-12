package dgu.se.bananavote.vote_info_service.District;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService {

    private final DistrictRepository districtRepository;

    @Autowired
    public DistrictService(DistrictRepository districtRepository) { this.districtRepository = districtRepository; }

    public District saveDistrict(District district) {
        return districtRepository.save(district);
    }

    public List<District> getDistrict() { return districtRepository.findAll(); }
    public List<District> getDistrictByWiwName(String wiwName) { return districtRepository.findByWiwName(wiwName); }
    public List<District> getDistrictBySdName(String sdName) { return districtRepository.findBySdName(sdName); }
}
