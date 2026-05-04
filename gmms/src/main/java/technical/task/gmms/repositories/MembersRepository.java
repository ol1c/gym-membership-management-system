package technical.task.gmms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import technical.task.gmms.entities.Member;
import technical.task.gmms.entities.MembershipPlan;

import java.util.List;
import java.util.UUID;

@Repository
public interface MembersRepository extends JpaRepository<Member, UUID> {
    List<Member> findAllByMembership(MembershipPlan membershipPlan);
    List<Member> findAllByMembershipId(UUID membershipId);
}
