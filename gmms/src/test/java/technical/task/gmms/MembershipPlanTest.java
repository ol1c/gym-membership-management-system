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
import technical.task.gmms.dto.MembershipPlanRequest;
import technical.task.gmms.entities.Address;
import technical.task.gmms.entities.Gym;
import technical.task.gmms.entities.MembershipType;
import technical.task.gmms.repositories.GymRepository;
import technical.task.gmms.repositories.MembershipPlanRepository;

import java.math.BigDecimal;
import java.util.Currency;
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
        Gym gym = gymRepository.save(new Gym(
                UUID.randomUUID(),
                "Gym",
                "123456789",
                new Address(
                        "Poland",
                        "00-000",
                        "Warsaw",
                        "Street 1"
                )));
        gymId = gym.getId();
    }

    @Test
    void createAndRetrieveMembershipPlan() throws Exception {
        MembershipPlanRequest request = new MembershipPlanRequest(
                "Plan",
                MembershipType.BASIC,
                new BigDecimal("99.99"),
                Currency.getInstance("PLN"),
                12,
                1
        );

        // Creating new membership plan
        MvcResult postResult = mockMvc.perform(post("/api/membership-plans/gyms/{gymId}", gymId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
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
        MembershipPlanRequest request = new MembershipPlanRequest(
                "Plan",
                MembershipType.BASIC,
                new BigDecimal("99.99"),
                Currency.getInstance("PLN"),
                12,
                1
        );

        // Creating new membership plan
        MvcResult postResult = mockMvc.perform(post("/api/membership-plans/gyms/{gymId}", gymId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = postResult.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(responseBody);
        String savedMembershipPlanId = root.path("id").asText();

        assertThat(membershipPlanRepository.count()).isEqualTo(1);

        // Try to get list of membership plans
        // List should be a JSON array with first element id == savedMembershipPlanId
        mockMvc.perform(get("/api/membership-plans/{id}", savedMembershipPlanId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(savedMembershipPlanId));
    }

    @Test
    void createMembershipPlanWithMonthlyPriceMoreThan2Decimals() throws Exception {
        MembershipPlanRequest invalidRequest = new MembershipPlanRequest(
                "Plan",
                MembershipType.BASIC,
                new BigDecimal("99.999"),
                Currency.getInstance("PLN"),
                12,
                1
        );

        // Attempt to create a membership plan with invalid monthly price amount
        mockMvc.perform(post("/api/membership-plans/gyms/{gymId}", gymId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        assertThat(membershipPlanRepository.count()).isEqualTo(0);
    }

    @Test
    void createMembershipPlanWithMonthlyPriceToBig() throws Exception {
        MembershipPlanRequest invalidRequest = new MembershipPlanRequest(
                "Plan",
                MembershipType.BASIC,
                new BigDecimal("999999999.99"),
                Currency.getInstance("PLN"),
                12,
                1
        );

        // Attempt to create a membership plan with invalid monthly price amount
        mockMvc.perform(post("/api/membership-plans/gyms/{gymId}", gymId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        assertThat(membershipPlanRepository.count()).isEqualTo(0);
    }

    @Test
    void createMembershipPlanWithNegativeMonthlyPrice() throws Exception {
        MembershipPlanRequest invalidRequest = new MembershipPlanRequest(
                "Plan",
                MembershipType.BASIC,
                new BigDecimal("-99.99"),
                Currency.getInstance("PLN"),
                12,
                1
        );

        // Attempt to create a membership plan with invalid monthly price amount
        mockMvc.perform(post("/api/membership-plans/gyms/{gymId}", gymId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        assertThat(membershipPlanRepository.count()).isEqualTo(0);
    }

    @Test
    void createMembershipPlanWithInvalidMonthlyPriceCurrency() throws Exception {
        MembershipPlanRequest invalidRequest = new MembershipPlanRequest(
                "Plan",
                MembershipType.BASIC,
                new BigDecimal("99.99"),
                Currency.getInstance("BTC"),
                12,
                1
        );

        // Attempt to create a membership plan with invalid monthly price amount
        mockMvc.perform(post("/api/membership-plans/gyms/{gymId}", gymId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        assertThat(membershipPlanRepository.count()).isEqualTo(0);
    }
}
