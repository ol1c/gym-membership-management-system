package technical.task.gmms.services;

import jakarta.persistence.EntityNotFoundException;
import technical.task.gmms.entities.Address;
import technical.task.gmms.entities.Member;
import technical.task.gmms.entities.MembershipPlan;
import technical.task.gmms.entities.MembershipType;
import technical.task.gmms.exceptions.MembershipCapacityExceededException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public interface MemberService {
    List<Member> findAll();

    List<Member> findAllByMembershipPlanId(UUID membershipPlanId);

    Member findById(UUID id) throws EntityNotFoundException;

    boolean existById(UUID id);

    long count();

    Member create(String firstName, String secondName, String lastName, String email,
                  String country, String zipCode, String city, String address, UUID membershipPlanId) throws MembershipCapacityExceededException;

    Member update(UUID id, String firstName, String secondName, String lastName, String email,
                  String country, String zipCode, String city, String address) throws NoSuchElementException;

    List<Member> saveAll(Iterable<Member> members);

    void delete(Member member);

    void deleteById(UUID id);
}
