package dgu.se.bananavote.vote_info_service.policy;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PolicyService {

    private final PolicyRepository policyRepository;

    @Autowired
    public PolicyService(PolicyRepository policyRepository) { this.policyRepository = policyRepository; }

    public List<Policy> getPolicy() { return policyRepository.findAll(); }
    public Optional<Policy> getPolicyById(int id) { return policyRepository.findById(id); }
    public List<Policy> getPolicyByJdName(String jdName) { return policyRepository.findByJdName(jdName); }

    public boolean existsByPartyNameAndPrmsOrder(String jdName, int prmsOrder) {
        return policyRepository.existsByJdNameAndPrmsOrder(jdName, prmsOrder);
    }

    public Policy savePolicy(Policy policy) {
        return policyRepository.save(policy);
    }
}
