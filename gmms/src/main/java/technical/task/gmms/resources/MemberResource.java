package technical.task.gmms.resources;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import technical.task.gmms.dto.MemberRequest;
import technical.task.gmms.dto.MemberResponse;
import technical.task.gmms.services.MemberService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/members")
public class MemberResource {
    @Autowired
    private MemberService memberService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<MemberResponse> getAllMembers() {
        return memberService.findAll().stream().map(MemberResponse::new).toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MemberResponse getMemberById(@PathVariable UUID id) {
        return new MemberResponse(memberService.findById(id));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MemberResponse updateMember(@PathVariable UUID id, @Valid @RequestBody MemberRequest member) {
        return new MemberResponse(memberService.update(
                id,
                member.getFirstName(),
                member.getSecondName(),
                member.getLastName(),
                member.getEmail(),
                member.getCountry(),
                member.getZipCode(),
                member.getCity(),
                member.getAddress()
        ));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MemberResponse cancelMembershipByMemberId(@PathVariable UUID id) {
        return new MemberResponse(memberService.cancelMembershipById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteMemberById(@PathVariable UUID id) {
        memberService.deleteById(id);
    }

    @GetMapping("/membership-plans/{membershipPlanId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<MemberResponse> getAllMembersByMembershipPlanId(@PathVariable UUID membershipPlanId) {
        return memberService.findAllByMembershipPlanId(membershipPlanId).stream().map(MemberResponse::new).toList();
    }

    @PostMapping("/membership-plans/{membershipPlanId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public MemberResponse createMember(@PathVariable UUID membershipPlanId, @Valid @RequestBody MemberRequest member) {
        return new MemberResponse(memberService.create(
                member.getFirstName(),
                member.getSecondName(),
                member.getLastName(),
                member.getEmail(),
                member.getCountry(),
                member.getZipCode(),
                member.getCity(),
                member.getAddress(),
                membershipPlanId
        ));
    }
}
