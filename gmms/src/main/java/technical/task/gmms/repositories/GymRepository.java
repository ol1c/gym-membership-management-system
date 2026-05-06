package technical.task.gmms.repositories;

import technical.task.gmms.entities.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GymRepository extends JpaRepository<Gym, UUID> {
    boolean existsByName(String name);
}
