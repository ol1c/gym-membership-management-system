package technical.task.gmms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import technical.task.gmms.entities.Gym;
import technical.task.gmms.entities.MembershipPlan;

import java.util.List;
import java.util.UUID;

@Repository
public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, UUID> {
    List<MembershipPlan> findAllByGym(Gym gym);
    List<MembershipPlan> findAllByGymId(UUID gymId);
}