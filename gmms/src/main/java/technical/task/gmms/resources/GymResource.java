package technical.task.gmms.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import technical.task.gmms.dto.GymRequest;
import technical.task.gmms.dto.GymResponse;
import technical.task.gmms.services.GymService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gyms")
public class GymResource {
    @Autowired
    private GymService gymService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<GymResponse> getAllGyms() {
        return gymService.findAll().stream().map(GymResponse::new).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GymResponse createGym(@Valid @RequestBody GymRequest gym) {
        return new GymResponse(gymService.create(
                gym.getName(),
                gym.getPhoneNumber(),
                gym.getCountry(),
                gym.getZipCode(),
                gym.getCity(),
                gym.getAddress()
        ));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GymResponse getGymById(@PathVariable UUID id) {
        return new GymResponse(gymService.findById(id));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GymResponse updateGym(@PathVariable UUID id, @Valid @RequestBody GymRequest gym) {
        return new GymResponse(gymService.update(
                id,
                gym.getName(),
                gym.getPhoneNumber(),
                gym.getCountry(),
                gym.getZipCode(),
                gym.getCity(),
                gym.getAddress()
        ));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteGym(@PathVariable UUID id) {
        gymService.deleteById(id);
    }
}
