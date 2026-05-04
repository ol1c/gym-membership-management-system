package technical.task.gmms.services;

import jakarta.persistence.EntityNotFoundException;
import technical.task.gmms.entities.Gym;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public interface GymService {
    List<Gym> findAll();

    Gym findById(UUID id) throws EntityNotFoundException;

    boolean exisitById(UUID id);

    long count();

    Gym create(String name, String phoneNumber, String country, String zipCode, String city, String address);

    Gym update(UUID id, String name, String phoneNumber, String country, String zipCode, String city, String address) throws NoSuchElementException;

    List<Gym> saveAll(Iterable<Gym> gyms);

    void delete(Gym gym);

    void deleteById(UUID id);
}
