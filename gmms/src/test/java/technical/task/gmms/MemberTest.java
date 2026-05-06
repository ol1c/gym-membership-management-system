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
import technical.task.gmms.entities.*;
import technical.task.gmms.repositories.GymRepository;
import technical.task.gmms.repositories.MemberRepository;
import technical.task.gmms.repositories.MembershipPlanRepository;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private MembershipPlanRepository membershipPlanRepository;

    @Autowired
    private MemberRepository memberRepository;

    private UUID membershipPlanId;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
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

        MembershipPlan membershipPlan = membershipPlanRepository.save(new MembershipPlan(
                UUID.randomUUID(),
                "Plan",
                MembershipType.BASIC,
                new Price(
                    new BigDecimal("99.99"),
                    Currency.getInstance("PLN")
                ),
                12,
                2,
                gym
        ));
        membershipPlanId = membershipPlan.getId();
    }

    @Test
    void createAndRetrieveMember() throws Exception {
        String jsonRequest = """
                {
                  "firstName": "Jan",
                  "secondName": "",
                  "lastName": "Kowalski",
                  "email": "jan.kowalski@example.com",
                  "country": "Poland",
                  "zipCode": "00-000",
                  "city": "Warsaw",
                  "address": "Street 10"
                }
            """;

        // Creating new member
        MvcResult postResult = mockMvc.perform(post("/api/members/membership-plans/{membershipPlanId}", membershipPlanId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = postResult.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(responseBody);
        String savedMemberId = root.path("id").asText();

        assertThat(membershipPlanRepository.count()).isEqualTo(1);

        // Getting a member entity with id = savedMemberId
        mockMvc.perform(get("/api/members/{id}", savedMemberId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedMemberId))
                .andExpect(jsonPath("$.firstName").value("Jan"))
                .andExpect(jsonPath("$.secondName").value(""))
                .andExpect(jsonPath("$.lastName").value("Kowalski"))
                .andExpect(jsonPath("$.email").value("jan.kowalski@example.com"))
                .andExpect(jsonPath("$.country").value("Poland"))
                .andExpect(jsonPath("$.zipCode").value("00-000"))
                .andExpect(jsonPath("$.city").value("Warsaw"))
                .andExpect(jsonPath("$.address").value("Street 10"))
                .andExpect(jsonPath("$.membershipId").value(membershipPlanId.toString()));
    }

    @Test
    void getMembershipPlanList() throws Exception {
        String jsonRequest = """
                {
                  "firstName": "Jan",
                  "secondName": "",
                  "lastName": "Kowalski",
                  "email": "jan.kowalski@example.com",
                  "country": "Poland",
                  "zipCode": "00-000",
                  "city": "Warsaw",
                  "address": "Street 10"
                }
            """;

        // Creating new member
        MvcResult postResult = mockMvc.perform(post("/api/members/membership-plans/{membershipPlanId}", membershipPlanId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = postResult.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(responseBody);
        String savedMemberId = root.path("id").asText();

        assertThat(membershipPlanRepository.count()).isEqualTo(1);

        // Try to get list of members
        // List should be a JSON array with first element id == savedMemberId
        mockMvc.perform(get("/api/members")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(savedMemberId));
    }

    @Test
    void createMembersWithSameEmails() throws Exception {
        String jsonRequest = """
                {
                  "firstName": "Jan",
                  "secondName": "",
                  "lastName": "Kowalski",
                  "email": "jan.kowalski@example.com",
                  "country": "Poland",
                  "zipCode": "00-000",
                  "city": "Warsaw",
                  "address": "Street 10"
                }
            """;

        mockMvc.perform(post("/api/members/membership-plans/{membershipPlanId}", membershipPlanId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        assertThat(gymRepository.count()).isEqualTo(1);

        String invalidJsonRequest = """
                {
                  "firstName": "Jan",
                  "secondName": "Jakub",
                  "lastName": "Kowalski",
                  "email": "jan.kowalski@example.com",
                  "country": "Poland",
                  "zipCode": "00-000",
                  "city": "Warsaw",
                  "address": "Street 10"
                }
            """;

        // Attempt to create a member with the same email
        mockMvc.perform(post("/api/members/membership-plans/{membershipPlanId}", membershipPlanId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());

        assertThat(gymRepository.count()).isEqualTo(1);
    }

    @Test
    void createMemberWithInvalidEmail() throws Exception {
        String invalidJsonRequest = """
                {
                  "firstName": "Jan",
                  "secondName": "",
                  "lastName": "Kowalski",
                  "email": "jan.kowalski.example.com",
                  "country": "Poland",
                  "zipCode": "00-000",
                  "city": "Warsaw",
                  "address": "Street 10"
                }
            """;

        // Attempt to create a member with invalid email
        mockMvc.perform(post("/api/members/membership-plans/{membershipPlanId}", membershipPlanId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());

        assertThat(memberRepository.count()).isEqualTo(0);
    }

    @Test
    void createMemberWithInvalidZipCode() throws Exception {
        String invalidJsonRequest = """
                {
                  "firstName": "Jan",
                  "secondName": "",
                  "lastName": "Kowalski",
                  "email": "jan.kowalski@example.com",
                  "country": "Poland",
                  "zipCode": "bad-code",
                  "city": "Warsaw",
                  "address": "Street 10"
                }
            """;

        // Attempt to create a member with invalid zip code
        mockMvc.perform(post("/api/members/membership-plans/{membershipPlanId}", membershipPlanId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());

        assertThat(memberRepository.count()).isEqualTo(0);
    }

    @Test
    void createMemberWithEmptyFirstName() throws Exception {
        String invalidJsonRequest = """
                {
                  "firstName": "",
                  "secondName": "",
                  "lastName": "Kowalski",
                  "email": "jan.kowalski@example.com",
                  "country": "Poland",
                  "zipCode": "00-000",
                  "city": "Warsaw",
                  "address": "Street 10"
                }
            """;

        // Attempt to create a member with empty first name
        mockMvc.perform(post("/api/members/membership-plans/{membershipPlanId}", membershipPlanId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());

        assertThat(memberRepository.count()).isEqualTo(0);
    }

    @Test
    void createMemberWithEmptyLastName() throws Exception {
        String invalidJsonRequest = """
                {
                  "firstName": "Jan",
                  "secondName": "",
                  "lastName": "",
                  "email": "jan.kowalski@example.com",
                  "country": "Poland",
                  "zipCode": "00-000",
                  "city": "Warsaw",
                  "address": "Street 10"
                }
            """;

        // Attempt to create a member with empty last name
        mockMvc.perform(post("/api/members/membership-plans/{membershipPlanId}", membershipPlanId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());

        assertThat(memberRepository.count()).isEqualTo(0);
    }

    @Test
    void createMemberWithEmptyEmail() throws Exception {
        String invalidJsonRequest = """
                {
                  "firstName": "Jan",
                  "secondName": "",
                  "lastName": "Kowalski",
                  "email": "",
                  "country": "Poland",
                  "zipCode": "00-000",
                  "city": "Warsaw",
                  "address": "Street 10"
                }
            """;

        // Attempt to create a member with empty email
        mockMvc.perform(post("/api/members/membership-plans/{membershipPlanId}", membershipPlanId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());

        assertThat(memberRepository.count()).isEqualTo(0);
    }

    @Test
    void createMemberWithEmptyCountry() throws Exception {
        String invalidJsonRequest = """
                {
                  "firstName": "Jan",
                  "secondName": "",
                  "lastName": "Kowalski",
                  "email": "jan.kowalski@example.com",
                  "country": "",
                  "zipCode": "00-000",
                  "city": "Warsaw",
                  "address": "Street 10"
                }
            """;

        // Attempt to create a member with empty country
        mockMvc.perform(post("/api/members/membership-plans/{membershipPlanId}", membershipPlanId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());

        assertThat(memberRepository.count()).isEqualTo(0);
    }

    @Test
    void createMemberWithEmptyZipCode() throws Exception {
        String invalidJsonRequest = """
                {
                  "firstName": "Jan",
                  "secondName": "",
                  "lastName": "Kowalski",
                  "email": "jan.kowalski@example.com",
                  "country": "Poland",
                  "zipCode": "",
                  "city": "Warsaw",
                  "address": "Street 10"
                }
            """;

        // Attempt to create a member with empty zip code
        mockMvc.perform(post("/api/members/membership-plans/{membershipPlanId}", membershipPlanId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());

        assertThat(memberRepository.count()).isEqualTo(0);
    }

    @Test
    void createMemberWithEmptyCity() throws Exception {
        String invalidJsonRequest = """
                {
                  "firstName": "Jan",
                  "secondName": "",
                  "lastName": "Kowalski",
                  "email": "jan.kowalski@example.com",
                  "country": "Poland",
                  "zipCode": "00-000",
                  "city": "",
                  "address": "Street 10"
                }
            """;

        // Attempt to create a member with empty city
        mockMvc.perform(post("/api/members/membership-plans/{membershipPlanId}", membershipPlanId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());

        assertThat(memberRepository.count()).isEqualTo(0);
    }

    @Test
    void createMemberWithEmptyAddress() throws Exception {
        String invalidJsonRequest = """
                {
                  "firstName": "Jan",
                  "secondName": "",
                  "lastName": "Kowalski",
                  "email": "jan.kowalski@example.com",
                  "country": "Poland",
                  "zipCode": "00-000",
                  "city": "Warsaw",
                  "address": ""
                }
            """;

        // Attempt to create a member with empty address
        mockMvc.perform(post("/api/members/membership-plans/{membershipPlanId}", membershipPlanId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                .andExpect(status().isBadRequest());

        assertThat(memberRepository.count()).isEqualTo(0);
    }

    @Test
    void updateExistingMember() throws Exception {
        // Create new member
        String jsonRequest = """
                {
                  "firstName": "Jan",
                  "secondName": "",
                  "lastName": "Kowalski",
                  "email": "jan.kowalski@example.com",
                  "country": "Poland",
                  "zipCode": "00-000",
                  "city": "Warsaw",
                  "address": "Street 10"
                }
            """;
        MvcResult postResult = mockMvc.perform(post("/api/members/membership-plans/{membershipPlanId}", membershipPlanId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        String savedMemberId = objectMapper.readTree(postResult.getResponse().getContentAsString()).path("id").asText();

        // Try to add second name
        String updateJsonRequest = """
                {
                  "firstName": "Jan",
                  "secondName": "Jakub",
                  "lastName": "Kowalski",
                  "email": "jan.kowalski@example.com",
                  "country": "Poland",
                  "zipCode": "00-000",
                  "city": "Warsaw",
                  "address": "Street 10"
                }
            """;
        mockMvc.perform(put("/api/members/{id}", savedMemberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedMemberId))
                .andExpect(jsonPath("$.firstName").value("Jan"))
                .andExpect(jsonPath("$.secondName").value("Jakub"))
                .andExpect(jsonPath("$.lastName").value("Kowalski"))
                .andExpect(jsonPath("$.email").value("jan.kowalski@example.com"))
                .andExpect(jsonPath("$.country").value("Poland"))
                .andExpect(jsonPath("$.zipCode").value("00-000"))
                .andExpect(jsonPath("$.city").value("Warsaw"))
                .andExpect(jsonPath("$.address").value("Street 10"))
                .andExpect(jsonPath("$.membershipId").value(membershipPlanId.toString()));

        assertThat(memberRepository.count()).isEqualTo(1)               ;
    }

    @Test
    void updateNonExistingMember() throws Exception {
        String savedMemberId = "nonExistingMembershipPlan";

        // Try to add a second name to non-existing member
        String updateJsonRequest = """
                {
                  "firstName": "Jan",
                  "secondName": "Jakub",
                  "lastName": "Kowalski",
                  "email": "jan.kowalski@example.com",
                  "country": "Poland",
                  "zipCode": "00-000",
                  "city": "Warsaw",
                  "address": "Street 10"
                }
            """;
        mockMvc.perform(put("/api/members/{id}", savedMemberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJsonRequest))
                .andExpect(status().isBadRequest());

        assertThat(memberRepository.count()).isEqualTo(0);
    }

    @Test
    void deleteExistingMember() throws Exception {
        // Create new member
        String jsonRequest = """
                {
                  "firstName": "Jan",
                  "secondName": "",
                  "lastName": "Kowalski",
                  "email": "jan.kowalski@example.com",
                  "country": "Poland",
                  "zipCode": "00-000",
                  "city": "Warsaw",
                  "address": "Street 10"
                }
            """;
        MvcResult postResult = mockMvc.perform(post("/api/members/membership-plans/{membershipPlanId}", membershipPlanId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        String savedMemberId = objectMapper.readTree(postResult.getResponse().getContentAsString()).path("id").asText();

        assertThat(memberRepository.count()).isEqualTo(1);

        // Try to delete the member
        mockMvc.perform(delete("/api/members/{id}", savedMemberId))
                .andExpect(status().isNoContent());

        assertThat(memberRepository.count()).isEqualTo(0);
    }

    @Test
    void deleteNonExistingMember() throws Exception {
        String savedMemberId = "nonExistingMembershipPlan";

        // Try to delete the non-existing member
        mockMvc.perform(delete("/api/members/{id}", savedMemberId))
                .andExpect(status().isBadRequest());

        assertThat(memberRepository.count()).isEqualTo(0);
    }

    @Test
    void cancelMembership() throws Exception {
        // Create new member
        String jsonRequest = """
                    {
                      "firstName": "Jan",
                      "secondName": "",
                      "lastName": "Kowalski",
                      "email": "jan.kowalski@example.com",
                      "country": "Poland",
                      "zipCode": "00-000",
                      "city": "Warsaw",
                      "address": "Street 10"
                    }
                """;
        MvcResult postResult = mockMvc.perform(post("/api/members/membership-plans/{membershipPlanId}", membershipPlanId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        String savedMemberId = objectMapper.readTree(postResult.getResponse().getContentAsString()).path("id").asText();

        assertThat(memberRepository.count()).isEqualTo(1);

        // Try to cancel the membership plan for the member
        mockMvc.perform(patch("/api/members/{id}", savedMemberId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedMemberId))
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }



    @Test
    void cancelMembershipForNonExistingMember() throws Exception {
        String savedMemberId = "nonExistingMembershipPlan";

        // Try to cancel the membership plan for the non-existing member
        mockMvc.perform(patch("/api/members/{id}", savedMemberId))
                .andExpect(status().isBadRequest());

        assertThat(memberRepository.count()).isEqualTo(0);
    }
}
