package technical.task.gmms.services;

import jakarta.persistence.EntityNotFoundException;
import technical.task.gmms.entities.MembershipPlan;
import technical.task.gmms.entities.MembershipStatus;
import technical.task.gmms.entities.MembershipType;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public interface MembershipPlanService {
    List<MembershipPlan> findAll();

    List<MembershipPlan> findAllByGymId(UUID gymId);

    MembershipPlan findById(UUID id) throws EntityNotFoundException;

    boolean exisitById(UUID id);

    long count();

    MembershipPlan create(String name, MembershipType type,
                          BigDecimal monthlyPriceAmount, Currency monthlyPriceCurrency, Integer duration, Integer maxMembers, UUID gymId);

    MembershipPlan update(UUID id, String name, MembershipType type,
                          BigDecimal monthlyPriceAmount, Currency monthlyPriceCurrency,
                          Integer duration, Integer maxMembers) throws NoSuchElementException;

    List<MembershipPlan> saveAll(Iterable<MembershipPlan> membershipPlans);

    void delete(MembershipPlan membershipPlan);

    void deleteById(UUID id);
}
