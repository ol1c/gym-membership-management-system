package technical.task.gmms.services;

import jakarta.persistence.EntityNotFoundException;
import technical.task.gmms.entities.MembershipPlan;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public interface MembershipPlansService {
    List<MembershipPlan> findAll();

    MembershipPlan findById(UUID id) throws EntityNotFoundException;

    boolean exisitById(UUID id);

    long count();

    MembershipPlan create(String name, String phoneNumber, String country, String zipCode, String city, String address);

    MembershipPlan update(UUID id, String name, String phoneNumber, String country, String zipCode, String city, String address) throws NoSuchElementException;

    List<MembershipPlan> saveAll(Iterable<MembershipPlan> gyms);

    void delete(MembershipPlan gym);

    void deleteById(UUID id);
}
