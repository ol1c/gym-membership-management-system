package technical.task.gmms.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import technical.task.gmms.entities.Address;
import technical.task.gmms.entities.Gym;
import technical.task.gmms.entities.MembershipPlan;
import technical.task.gmms.report.GymCurrencyKey;
import technical.task.gmms.report.GymReport;
import technical.task.gmms.repositories.GymRepository;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class GymServiceImpl implements GymService{
    @Autowired
    GymRepository gymRepository;

    @Override
    public List<Gym> findAll() {
        return gymRepository.findAll();
    }

    @Override
    public Gym findById(UUID id) throws EntityNotFoundException {
        return gymRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gym with id " + id + " not found"));
    }

    @Override
    public boolean existById(UUID id) {
        return gymRepository.existsById(id);
    }

    @Override
    public long count() {
        return gymRepository.count();
    }

    @Override
    public Gym create(String name, String phoneNumber, String country, String zipCode, String city, String address) {
        if (gymRepository.existsByName(name)) {
            throw new IllegalArgumentException("Gym with this name already exist");
        }
        Gym gym = new Gym(
                UUID.randomUUID(),
                name,
                phoneNumber,
                new Address(country, zipCode, city, address)
        );
        return gymRepository.save(gym);
    }

    @Override
    public Gym update(UUID id, String name, String phoneNumber, String country, String zipCode, String city, String address) throws NoSuchElementException{
        Gym gym = gymRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Gym with id " + id + " not found"));
        gym.setName(name);
        gym.setPhoneNumber(phoneNumber);
        gym.setAddress(new Address(country, zipCode, city, address));
        return gymRepository.save(gym);
    }

    @Override
    public List<Gym> saveAll(Iterable<Gym> gyms) {
        return gymRepository.saveAll(gyms);
    }

    @Override
    public void delete(Gym gym) {
        gymRepository.delete(gym);
    }

    @Override
    public void deleteById(UUID id) {
        gymRepository.deleteById(id);
    }

    @Override
    public List<GymReport> createReport() {
        List<Gym> gyms = gymRepository.findAll();
        Map<GymCurrencyKey, BigDecimal> revenueMap = new HashMap<>();
        for (Gym gym : gyms) {
            List<MembershipPlan> membershipPlans = gym.getMembershipPlans();
            for (MembershipPlan membershipPlan : membershipPlans) {
                GymCurrencyKey key = new GymCurrencyKey(gym.getName(), membershipPlan.getMonthlyPrice().getCurrency());
                BigDecimal revenue = membershipPlan.getMonthlyPrice().getAmount()
                        .multiply(BigDecimal.valueOf(membershipPlan.countAllActiveMembers()));
                revenueMap.merge(key, revenue, BigDecimal::add);
            }
        }
        return revenueMap.entrySet().stream().map(entry -> new GymReport(
                entry.getKey().gymName(),
                entry.getValue(),
                entry.getKey().currency()
                ))
                .sorted(Comparator.comparing(GymReport::gymName)
                        .thenComparing(report -> report.currency().getCurrencyCode()))
                .toList();
    }
}
