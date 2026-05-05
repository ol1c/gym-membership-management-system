package technical.task.gmms.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import technical.task.gmms.dto.MembershipPlanRequest;
import technical.task.gmms.dto.MembershipPlanResponse;
import technical.task.gmms.services.MembershipPlanService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/mebership-plans")
public class MembershipPlanResource {
    @Autowired
    private MembershipPlanService membershipPlanService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<MembershipPlanResponse> getAllMembershipPlans() {
        return membershipPlanService.findAll().stream().map(MembershipPlanResponse::new).toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MembershipPlanResponse getMembershipPlanById(@PathVariable UUID id) {
        return new MembershipPlanResponse(membershipPlanService.findById(id));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MembershipPlanResponse updateMembershipPlan(@PathVariable UUID id, @RequestBody MembershipPlanRequest membershipPlan) {
        return new MembershipPlanResponse(membershipPlanService.update(
                id,
                membershipPlan.getName(),
                membershipPlan.getType(),
                membershipPlan.getMonthlyPriceAmount(),
                membershipPlan.getMonthlyPriceCurrency(),
                membershipPlan.getDuration(),
                membershipPlan.getMaxMembers()
        ));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteMembershipPlan(@PathVariable UUID id) {
        membershipPlanService.deleteById(id);
    }


    @GetMapping("/gyms/{gymId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<MembershipPlanResponse> getMembershipPlansByGymId(@PathVariable UUID gymId) {
        return membershipPlanService.findAllByGymId(gymId).stream().map(MembershipPlanResponse::new).toList();
    }

    @PostMapping("/gyms/{gymId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public MembershipPlanResponse createMembershipPlan(@PathVariable UUID gymId, @RequestBody MembershipPlanRequest membershipPlan) {
        return new MembershipPlanResponse(membershipPlanService.create(
                membershipPlan.getName(),
                membershipPlan.getType(),
                membershipPlan.getMonthlyPriceAmount(),
                membershipPlan.getMonthlyPriceCurrency(),
                membershipPlan.getDuration(),
                membershipPlan.getMaxMembers(),
                gymId
        ));
    }
}
