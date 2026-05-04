package technical.task.gmms.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import technical.task.gmms.entities.Gym;
import technical.task.gmms.entities.MembershipPlan;
import technical.task.gmms.entities.MembershipType;
import technical.task.gmms.entities.Price;
import technical.task.gmms.repositories.MembershipPlanRepository;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
public class MembershipPlanServiceImpl implements MembershipPlanService{
    @Autowired
    MembershipPlanRepository membershipPlanRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MembershipPlan> findAll() {
        return membershipPlanRepository.findAll();
    }

    @Override
    public MembershipPlan findById(UUID id) throws EntityNotFoundException {
        return membershipPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Membership with id " + id + " not found"));
    }

    @Override
    public boolean exisitById(UUID id) {
        return membershipPlanRepository.existsById(id);
    }

    @Override
    public long count() {
        return membershipPlanRepository.count();
    }

    @Override
    public MembershipPlan create(String name, MembershipType type, BigDecimal monthlyPriceAmount, Currency monthlyPriceCurrency, Integer duration, Integer maxMembers, UUID gymId) {
        Gym gymRef = entityManager.getReference(Gym.class, gymId);
        MembershipPlan membershipPlan = new MembershipPlan(UUID.randomUUID(),
                name,
                type,
                new Price(monthlyPriceAmount, monthlyPriceCurrency),
                duration,
                maxMembers,
                gymRef);
        gymRef.addMembershipPlan(membershipPlan);
        return membershipPlanRepository.save(membershipPlan);
    }

    @Override
    public MembershipPlan update(UUID id, String name, MembershipType type, BigDecimal monthlyPriceAmount, Currency monthlyPriceCurrency, Integer duration, Integer maxMembers) throws NoSuchElementException {
        MembershipPlan membershipPlan = membershipPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Membership plan with id "+ id + " not found"));
        membershipPlan.setName(name);
        membershipPlan.setType(type);
        membershipPlan.setMonthlyPrice(new Price(monthlyPriceAmount, monthlyPriceCurrency));
        membershipPlan.setDuration(duration);
        membershipPlan.setMaxMembers(maxMembers);
        return membershipPlanRepository.save(membershipPlan);
    }

    @Override
    public List<MembershipPlan> saveAll(Iterable<MembershipPlan> membershipPlans) {
        return membershipPlanRepository.saveAll(membershipPlans);
    }

    @Override
    public void delete(MembershipPlan membershipPlan) {
        membershipPlanRepository.delete(membershipPlan);
    }

    @Override
    public void deleteById(UUID id) {
        membershipPlanRepository.deleteById(id);
    }
}
