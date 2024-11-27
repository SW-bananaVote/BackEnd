package dgu.se.bananavote.vote_info_service.policy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/policy")
public class PolicyController {

    private final PolicyService policyService;

    @Autowired
    public PolicyController(PolicyService policyService) { this.policyService = policyService; }

    @GetMapping
    public List<Policy> getPolicy() { return policyService.getPolicy(); }
    @GetMapping("/{id}")
    public Optional<Policy> getPolicyById(@PathVariable int id) { return policyService.getPolicyById(id); }
    @GetMapping("/jdName/{jdName}")
    public List<Policy> getPolicyByJdName(@PathVariable String jdName) { return policyService.getPolicyByJdName(jdName); }
}
