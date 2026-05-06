package technical.task.gmms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import technical.task.gmms.dto.GymRequest;
import technical.task.gmms.repositories.GymRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class GymTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GymRepository gymRepository;

    @BeforeEach
    void setUp() {
        gymRepository.deleteAll();
    }

    @Test
    void createAndRetrieveGym() throws Exception {
        GymRequest request = new GymRequest(
                "Gym",
                "123456789",
                "Poland",
                "00-000",
                "Warsaw",
                "Street 1"
        );

        // Creating new gym
        MvcResult postResult = mockMvc.perform(post("/api/gyms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = postResult.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(responseBody);
        String savedGymId = root.path("id").asText();

        assertThat(gymRepository.count()).isEqualTo(1);

        // Getting a gym entity with id = savedGymId
        mockMvc.perform(get("/api/gyms/{id}", savedGymId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedGymId))
                .andExpect(jsonPath("$.name").value("Gym"))
                .andExpect(jsonPath("$.phoneNumber").value("123456789"))
                .andExpect(jsonPath("$.country").value("Poland"))
                .andExpect(jsonPath("$.zipCode").value("00-000"))
                .andExpect(jsonPath("$.city").value("Warsaw"))
                .andExpect(jsonPath("$.address").value("Street 1"));
    }

    @Test
    void createGymWithInvalidZipCode() throws Exception {
        GymRequest invalidRequest = new GymRequest(
                "Gym",
                "123456789",
                "Poland",
                "Bad-code",
                "Warsaw",
                "Street 1"
        );

        // Attempt to create a gym with invalid zip code
        mockMvc.perform(post("/api/gyms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        assertThat(gymRepository.count()).isEqualTo(0);
    }

    @Test
    void createGymWithEmptyName() throws Exception {
        GymRequest invalidRequest = new GymRequest(
                "",
                "123456789",
                "Poland",
                "00-000",
                "Warsaw",
                "Street 1"
        );

        // Attempt to create a gym with empty name field
        mockMvc.perform(post("/api/gyms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        assertThat(gymRepository.count()).isEqualTo(0);
    }

    @Test
    void createGymWithEmptyPhoneNumber() throws Exception {
        GymRequest invalidRequest = new GymRequest(
                "Gym",
                "",
                "Poland",
                "00-000",
                "Warsaw",
                "Street 1"
        );

        // Attempt to create a gym with empty phone number field
        mockMvc.perform(post("/api/gyms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        assertThat(gymRepository.count()).isEqualTo(0);
    }

    @Test
    void createGymWithEmptyCountry() throws Exception {
        GymRequest invalidRequest = new GymRequest(
                "Gym",
                "123456789",
                "",
                "00-000",
                "Warsaw",
                "Street 1"
        );

        // Attempt to create a gym with empty country field
        mockMvc.perform(post("/api/gyms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        assertThat(gymRepository.count()).isEqualTo(0);
    }

    @Test
    void createGymWithEmptyZipCode() throws Exception {
        GymRequest invalidRequest = new GymRequest(
                "Gym",
                "123456789",
                "Poland",
                "",
                "Warsaw",
                "Street 1"
        );

        // Attempt to create a gym with empty zip code field
        mockMvc.perform(post("/api/gyms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        assertThat(gymRepository.count()).isEqualTo(0);
    }

    @Test
    void createGymWithEmptyCity() throws Exception {
        GymRequest invalidRequest = new GymRequest(
                "Gym",
                "123456789",
                "Poland",
                "00-000",
                "",
                "Street 1"
        );

        // Attempt to create a gym with empty city field
        mockMvc.perform(post("/api/gyms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        assertThat(gymRepository.count()).isEqualTo(0);
    }

    @Test
    void createGymWithEmptyAddress() throws Exception {
        GymRequest invalidRequest = new GymRequest(
                "Gym",
                "123456789",
                "Poland",
                "00-000",
                "Warsaw",
                ""
        );

        // Attempt to create a gym with empty address field
        mockMvc.perform(post("/api/gyms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        assertThat(gymRepository.count()).isEqualTo(0);
    }
    
    @Test
    void updateExistingGym() throws Exception {
        // Create new gym
        GymRequest createRequest = new GymRequest(
                "Gym",
                "123456789",
                "Poland",
                "00-000",
                "Warsaw",
                "Street 1"
        );
        MvcResult postResult = mockMvc.perform(post("/api/gyms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String savedGymId = objectMapper.readTree(postResult.getResponse().getContentAsString()).path("id").asText();

        // Try to change the name of the gym
        GymRequest updateRequest = new GymRequest(
                "New Gym",
                "123456789",
                "Poland",
                "00-000",
                "Warsaw",
                "Street 1"
        );
        mockMvc.perform(put("/api/gyms/{id}", savedGymId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Gym"))
                .andExpect(jsonPath("$.phoneNumber").value("123456789"))
                .andExpect(jsonPath("$.country").value("Poland"))
                .andExpect(jsonPath("$.zipCode").value("00-000"))
                .andExpect(jsonPath("$.city").value("Warsaw"))
                .andExpect(jsonPath("$.address").value("Street 1"));;

        assertThat(gymRepository.count()).isEqualTo(1);
    }

    @Test
    void updateNonExistingGym() throws Exception {
        String savedGymId = "nonExistingGym";

        // Try to change the name of the non-existing gym
        GymRequest updateRequest = new GymRequest(
                "New Gym",
                "123456789",
                "Poland",
                "00-000",
                "Warsaw",
                "Street 1"
        );
        mockMvc.perform(put("/api/gyms/{id}", savedGymId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());

        assertThat(gymRepository.count()).isEqualTo(0);
    }
}