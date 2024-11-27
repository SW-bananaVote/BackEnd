package dgu.se.bananavote.vote_info_service.candidate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CareerService {

    private final CareerRepository careerRepository;

    @Autowired
    public CareerService(CareerRepository careerRepository) {
        this.careerRepository = careerRepository;
    }

    public Career saveCareer(Career career) {
        return careerRepository.save(career);
    }

    public boolean existsByCnddtIdAndCareerOrder(String cnddtId, int careerOrder) {
        return this.careerRepository.existsByCnddtIdAndCareerOrder(cnddtId, careerOrder);
    }
}
