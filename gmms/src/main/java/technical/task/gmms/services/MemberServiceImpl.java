package technical.task.gmms.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import technical.task.gmms.entities.Address;
import technical.task.gmms.entities.Member;
import technical.task.gmms.entities.MembershipPlan;
import technical.task.gmms.entities.MembershipStatus;
import technical.task.gmms.exceptions.MembershipCapacityExceededException;
import technical.task.gmms.repositories.MemberRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
public class MemberServiceImpl implements MemberService{
    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    public List<Member> findAllByMembershipPlanId(UUID membershipPlanId) {
        return memberRepository.findAllByMembershipId(membershipPlanId);
    }

    @Override
    public Member findById(UUID id) throws EntityNotFoundException {
        return memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Member with id " + id + " not found"));
    }

    @Override
    public boolean existById(UUID id) {
        return memberRepository.existsById(id);
    }

    @Override
    public long count() {
        return memberRepository.count();
    }

    @Override
    public Member create(String firstName, String secondName, String lastName, String email, String country,
                         String zipCode, String city, String address, UUID membershipPlanId) throws MembershipCapacityExceededException {
        MembershipPlan membershipPlan = entityManager.getReference(MembershipPlan.class, membershipPlanId);
        Member member = new Member(
                UUID.randomUUID(),
                firstName,
                secondName,
                lastName,
                email,
                new Address(country, zipCode, city, address),
                membershipPlan
        );
        return memberRepository.save(member);
    }

    @Override
    public Member update(UUID id, String firstName, String secondName, String lastName, String email,
                         String country, String zipCode, String city, String address) throws NoSuchElementException {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Member with id \" + id + \" not found"));
        member.setFirstName(firstName);
        member.setSecondName(secondName);
        member.setLastName(lastName);
        member.setEmail(email);
        member.setAddress(new Address(country, zipCode, city, address));
        return memberRepository.save(member);
    }

    @Override
    public Member cancelMembershipById(UUID id) throws NoSuchElementException {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Member with id \" + id + \" not found"));
        member.setStatus(MembershipStatus.CANCELLED);
        return memberRepository.save(member);
    }

    @Override
    public List<Member> saveAll(Iterable<Member> members) {
        return memberRepository.saveAll(members);
    }

    @Override
    public void delete(Member member) {
        memberRepository.delete(member);
    }

    @Override
    public void deleteById(UUID id) {
        memberRepository.deleteById(id);
    }
}
