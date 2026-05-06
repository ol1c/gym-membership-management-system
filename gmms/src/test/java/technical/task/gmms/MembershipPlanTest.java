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
import technical.task.gmms.entities.Address;
import technical.task.gmms.entities.Gym;
import technical.task.gmms.entities.MembershipType;
import technical.task.gmms.repositories.GymRepository;
import technical.task.gmms.repositories.MembershipPlanRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MembershipPlanTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private MembershipPlanRepository membershipPlanRepository;

    private UUID gymId;

    @BeforeEach
    void setUp() {
        membershipPlanRepository.deleteAll();
        gymRepository.deleteAll();

        // Create a gym for further tests
        Gym gym = gymRepository.save(new Gym(
                UUID.randomUUID(),
                "Gym",
                "123456789",
                new Address(
                        "Poland",
                        "00-000",
                        "Warsaw",
                        "Street 1"
                )
        ));
        gymId = gym.getId();
    }

    @Test
    void createAndRetrieveMembershipPlan() throws Exception {
        String jsonRequest = """
            {
                "name": "Plan",
                "type": "BASIC",
                "monthlyPriceAmount": 99.99,
                "monthlyPriceCurrency": "PLN",
                "duration": 12,
                "maxMembers": 1
            }
            """;

        // Creating new membership plan
        MvcResult postResult = mockMvc.perform(post("/api/membership-plans/gyms/{gymId}", gymId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = postResult.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(responseBody);
        String savedMembershipPlanId = root.path("id").asText();

        assertThat(membershipPlanRepository.count()).isEqualTo(1);

        // Getting a membershipPlan entity with id = savedMembershipPlanId
        mockMvc.perform(get("/api/membership-plans/{id}", savedMembershipPlanId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedMembershipPlanId))
                .andExpect(jsonPath("$.name").value("Plan"))
                .andExpect(jsonPath("$.type").value(MembershipType.BASIC.toString()))
                .andExpect(jsonPath("$.monthlyPrice").value("99.99 PLN"))
                .andExpect(jsonPath("$.duration").value(12))
                .andExpect(jsonPath("$.maxMembers").value(1))
                .andExpect(jsonPath("$.gymId").value(gymId.toString()));
    }

    @Test
    void getMembershipPlanList() throws Exception {
        String jsonRequest = """
            {
                "name": "Plan",
                "type": "BASIC",
                "monthlyPriceAmount": 99.99,
                "monthlyPriceCurrency": "PLN",
                "duration": 12,
                "maxMembers": 1
            }
            """;

        // Creating new membership plan
        MvcResult postResult = mockMvc.perform(post("/api/membership-plans/gyms/{gymId}", gymId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = postResult.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(responseBody);
        String savedMembershipPlanId = root.path("id").asText();

        assertThat(membershipPlanRepository.count()).isEqualTo(1);

        // Try to get list of membership plans
        // List should be a JSON array with first element id == savedMembershipPlanId
        mockMvc.perform(get("/api/membership-plans/gyms/{gymId}", gymId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(savedMembershipPlanId));
    }

    @Test
    void createMembershipPlanWithMonthlyPriceMoreThan2Decimals() throws Exception {
        String invalidJsonRequest = """
            {
                "name": "Plan",
                "type": "BASIC",
                "monthlyPriceAmount": 99.999,
                "monthlyPriceCurrency": "PLN",
                "duration": 12,
                "maxMembers": 1
            }
            """;

        // Attempt to create a membership plan with invalid monthly price amount
        mockMvc.perform(post("/api/membership-plans/gyms/{gymId}", gymId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());

        assertThat(membershipPlanRepository.count()).isEqualTo(0);
    }

    @Test
    void createMembershipPlanWithMonthlyPriceToBig() throws Exception {
        String invalidJsonRequest = """
            {
                "name": "Plan",
                "type": "BASIC",
                "monthlyPriceAmount": 999999999.99,
                "monthlyPriceCurrency": "PLN",
                "duration": 12,
                "maxMembers": 1
            }
            """;

        // Attempt to create a membership plan with invalid monthly price amount
        mockMvc.perform(post("/api/membership-plans/gyms/{gymId}", gymId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());

        assertThat(membershipPlanRepository.count()).isEqualTo(0);
    }

    @Test
    void createMembershipPlanWithNegativeMonthlyPrice() throws Exception {
        String invalidJsonRequest = """
            {
                "name": "Plan",
                "type": "BASIC",
                "monthlyPriceAmount": -99.99,
                "monthlyPriceCurrency": "PLN",
                "duration": 12,
                "maxMembers": 1
            }
            """;

        // Attempt to create a membership plan with invalid monthly price amount
        mockMvc.perform(post("/api/membership-plans/gyms/{gymId}", gymId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());

        assertThat(membershipPlanRepository.count()).isEqualTo(0);
    }

    @Test
    void createMembershipPlanWithInvalidMonthlyPriceCurrency() throws Exception {
        String invalidJsonRequest = """
            {
                "name": "Plan",
                "type": "BASIC",
                "monthlyPriceAmount": 99.99,
                "monthlyPriceCurrency": "INV",
                "duration": 12,
                "maxMembers": 1
            }
            """;

        // Attempt to create a membership plan with invalid monthly price currency
        mockMvc.perform(post("/api/membership-plans/gyms/{gymId}", gymId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());

        assertThat(membershipPlanRepository.count()).isEqualTo(0);
    }

    @Test
    void createMembershipPlanWithNegativeDuration() throws Exception {
        String invalidJsonRequest = """
            {
                "name": "Plan",
                "type": "BASIC",
                "monthlyPriceAmount": 99.99,
                "monthlyPriceCurrency": "PLN",
                "duration": -12,
                "maxMembers": 1
            }
            """;

        // Attempt to create a membership plan with invalid duration
        mockMvc.perform(post("/api/membership-plans/gyms/{gymId}", gymId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());

        assertThat(membershipPlanRepository.count()).isEqualTo(0);
    }

    @Test
    void createMembershipPlanWithNegativeMaxMembers() throws Exception {
        String invalidJsonRequest = """
            {
                "name": "Plan",
                "type": "BASIC",
                "monthlyPriceAmount": 99.99,
                "monthlyPriceCurrency": "PLN",
                "duration": 12,
                "maxMembers": -1
            }
            """;

        // Attempt to create a membership plan with invalid max members
        mockMvc.perform(post("/api/membership-plans/gyms/{gymId}", gymId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());

        assertThat(membershipPlanRepository.count()).isEqualTo(0);
    }

    @Test
    void createMembershipPlanWithEmptyName() throws Exception {
        String invalidJsonRequest = """
            {
                "name": "",
                "type": "BASIC",
                "monthlyPriceAmount": 99.99,
                "monthlyPriceCurrency": "PLN",
                "duration": 12,
                "maxMembers": 1
            }
            """;

        // Attempt to create a membership plan with invalid max members
        mockMvc.perform(post("/api/membership-plans/gyms/{gymId}", gymId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());

        assertThat(membershipPlanRepository.count()).isEqualTo(0);
    }

    @Test
    void createMembershipPlanWithEmptyType() throws Exception {
        String invalidJsonRequest = """
            {
                "name": "Plan",
                "type": "",
                "monthlyPriceAmount": 99.99,
                "monthlyPriceCurrency": "PLN",
                "duration": 12,
                "maxMembers": 1
            }
            """;

        // Attempt to create a membership plan with invalid max members
        mockMvc.perform(post("/api/membership-plans/gyms/{gymId}", gymId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());

        assertThat(membershipPlanRepository.count()).isEqualTo(0);
    }

    @Test
    void createMembershipPlanWithEmptyMonthlyPriceAmount() throws Exception {
        String invalidJsonRequest = """
            {
                "name": "Plan",
                "type": "BASIC",
                "monthlyPriceAmount": ,
                "monthlyPriceCurrency": "PLN",
                "duration": 12,
                "maxMembers": 1
            }
            """;

        // Attempt to create a membership plan with invalid max members
        mockMvc.perform(post("/api/membership-plans/gyms/{gymId}", gymId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());

        assertThat(membershipPlanRepository.count()).isEqualTo(0);
    }

    @Test
    void createMembershipPlanWithEmptyMonthlyPriceCurrency() throws Exception {
        String invalidJsonRequest = """
            {
                "name": "Plan",
                "type": "BASIC",
                "monthlyPriceAmount": 99.99,
                "monthlyPriceCurrency": "",
                "duration": 12,
                "maxMembers": 1
            }
            """;

        // Attempt to create a membership plan with invalid max members
        mockMvc.perform(post("/api/membership-plans/gyms/{gymId}", gymId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());

        assertThat(membershipPlanRepository.count()).isEqualTo(0);
    }

    @Test
    void createMembershipPlanWithEmptyDuration() throws Exception {
        String invalidJsonRequest = """
            {
                "name": "Plan",
                "type": "BASIC",
                "monthlyPriceAmount": 99.99,
                "monthlyPriceCurrency": "PLN",
                "duration": ,
                "maxMembers": 1
            }
            """;

        // Attempt to create a membership plan with invalid max members
        mockMvc.perform(post("/api/membership-plans/gyms/{gymId}", gymId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());

        assertThat(membershipPlanRepository.count()).isEqualTo(0);
    }

    @Test
    void createMembershipPlanWithEmptyMaxMembers() throws Exception {
        String invalidJsonRequest = """
            {
                "name": "Plan",
                "type": "BASIC",
                "monthlyPriceAmount": 99.99,
                "monthlyPriceCurrency": "PLN",
                "duration": 12,
                "maxMembers": 0
            }
            """;

        // Attempt to create a membership plan with invalid max members
        mockMvc.perform(post("/api/membership-plans/gyms/{gymId}", gymId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());

        assertThat(membershipPlanRepository.count()).isEqualTo(0);
    }

    @Test
    void updateExistingGym() throws Exception {
        // Create new membership plan
        String jsonRequest = """
            {
                "name": "Plan",
                "type": "BASIC",
                "monthlyPriceAmount": 99.99,
                "monthlyPriceCurrency": "PLN",
                "duration": 12,
                "maxMembers": 1
            }
            """;
        MvcResult postResult = mockMvc.perform(post("/api/membership-plans/gyms/{gymId}", gymId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        String savedMembershipPlanId = objectMapper.readTree(postResult.getResponse().getContentAsString()).path("id").asText();

        // Try to change the name of the gym
        String updateJsonRequest = """
            {
                "name": "Plan 2",
                "type": "BASIC",
                "monthlyPriceAmount": 99.99,
                "monthlyPriceCurrency": "PLN",
                "duration": 12,
                "maxMembers": 1
            }
            """;
        mockMvc.perform(put("/api/membership-plans/{id}", savedMembershipPlanId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedMembershipPlanId))
                .andExpect(jsonPath("$.name").value("Plan 2"))
                .andExpect(jsonPath("$.type").value(MembershipType.BASIC.toString()))
                .andExpect(jsonPath("$.monthlyPrice").value("99.99 PLN"))
                .andExpect(jsonPath("$.duration").value(12))
                .andExpect(jsonPath("$.maxMembers").value(1))
                .andExpect(jsonPath("$.gymId").value(gymId.toString()));

        assertThat(membershipPlanRepository.count()).isEqualTo(1);
    }

    @Test
    void updateNonExistingGym() throws Exception {
        String savedMembershipPlanId = "nonExistingMembershipPlan";

        // Try to change the name of the non-existing gym
        String updateJsonRequest = """
            {
                "name": "Plan 2",
                "type": "BASIC",
                "monthlyPriceAmount": 99.99,
                "monthlyPriceCurrency": "PLN",
                "duration": 12,
                "maxMembers": 1
            }
            """;
        mockMvc.perform(put("/api/membership-plans/{id}", savedMembershipPlanId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJsonRequest))
                .andExpect(status().isBadRequest());

        assertThat(membershipPlanRepository.count()).isEqualTo(0);
    }

    @Test
    void deleteExistingGym() throws Exception {
        // Create new membership plan
        String jsonRequest = """
            {
                "name": "Plan",
                "type": "BASIC",
                "monthlyPriceAmount": 99.99,
                "monthlyPriceCurrency": "PLN",
                "duration": 12,
                "maxMembers": 1
            }
            """;
        MvcResult postResult = mockMvc.perform(post("/api/membership-plans/gyms/{gymId}", gymId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        String savedMembershipPlanId = objectMapper.readTree(postResult.getResponse().getContentAsString()).path("id").asText();

        assertThat(membershipPlanRepository.count()).isEqualTo(1);

        // Try to delete the gym
        mockMvc.perform(delete("/api/membership-plans/{id}", savedMembershipPlanId))
                .andExpect(status().isNoContent());

        assertThat(membershipPlanRepository.count()).isEqualTo(0);
    }

    @Test
    void deleteNonExistingGym() throws Exception {
        String savedMembershipPlanId = "nonExistingMembershipPlan";

        // Try to delete the non-existing gym
        mockMvc.perform(delete("/api/membership-plans/{id}", savedMembershipPlanId))
                .andExpect(status().isBadRequest());

        assertThat(membershipPlanRepository.count()).isEqualTo(0);
    }
}
