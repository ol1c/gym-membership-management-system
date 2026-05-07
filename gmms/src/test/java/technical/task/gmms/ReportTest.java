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
public class ReportTest {


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

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        membershipPlanRepository.deleteAll();
        gymRepository.deleteAll();
    }

    @Test
    void createReportForTwoGyms() throws Exception {
        // Create the first gym
        Gym firstGym = gymRepository.save(new Gym(
                UUID.randomUUID(),
                "Gym1",
                "123456789",
                new Address(
                        "Poland",
                        "00-000",
                        "Warsaw",
                        "Street 1"
                )
        ));

        // Create a membership plan
        MembershipPlan firstMembershipPlan = membershipPlanRepository.save(new MembershipPlan(
                UUID.randomUUID(),
                "Plan",
                MembershipType.BASIC,
                new Price(
                        new BigDecimal("24.99"),
                        Currency.getInstance("EUR")
                ),
                12,
                2,
                firstGym
        ));
        // Create member
        memberRepository.save(new Member(
                UUID.randomUUID(),
                "Jan",
                "",
                "Kowalski",
                "jan.kowalski@example.com",
                new Address(
                        "Poland",
                        "00-000",
                        "Warsaw",
                        "Street 10"
                ),
                firstMembershipPlan
        ));

        // Create the second gym, a membership plan and a member
        Gym secondGym = gymRepository.save(new Gym(
                UUID.randomUUID(),
                "Gym2",
                "123456789",
                new Address(
                        "Poland",
                        "00-000",
                        "Warsaw",
                        "Street 2"
                )
        ));
        MembershipPlan secondMembershipPlan = membershipPlanRepository.save(new MembershipPlan(
                UUID.randomUUID(),
                "Plan",
                MembershipType.BASIC,
                new Price(
                        new BigDecimal("24.99"),
                        Currency.getInstance("EUR")
                ),
                12,
                1,
                secondGym
        ));
        memberRepository.save(new Member(
                UUID.randomUUID(),
                "Jakub",
                "",
                "Nowak",
                "jakub.nowak@example.com",
                new Address(
                        "Poland",
                        "00-000",
                        "Warsaw",
                        "Street 20"
                ),
                secondMembershipPlan
        ));

        // Now there should be 2 gyms, 2 membership plans and 2 members
        assertThat(gymRepository.count()).isEqualTo(2);
        assertThat(membershipPlanRepository.count()).isEqualTo(2);
        assertThat(memberRepository.count()).isEqualTo(2);

        // Getting a report with 2 records
        mockMvc.perform(get("/api/gyms/reports")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].gymName").value("Gym1"))
                .andExpect(jsonPath("$[0].amount").value(24.99))
                .andExpect(jsonPath("$[0].currency").value("EUR"))
                .andExpect(jsonPath("$[1].gymName").value("Gym2"))
                .andExpect(jsonPath("$[1].amount").value(24.99))
                .andExpect(jsonPath("$[1].currency").value("EUR"));
    }

    @Test
    void createReportForGymWithTwoMembershipPlansInSameCurrencies() throws Exception {
        // Create a gym
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

        // Create the first membership plan with a member
        MembershipPlan firstMembershipPlan = membershipPlanRepository.save(new MembershipPlan(
                UUID.randomUUID(),
                "Plan1",
                MembershipType.BASIC,
                new Price(
                        new BigDecimal("24.99"),
                        Currency.getInstance("EUR")
                ),
                12,
                1,
                gym
        ));
        memberRepository.save(new Member(
                UUID.randomUUID(),
                "Jan",
                "",
                "Kowalski",
                "jan.kowalski@example.com",
                new Address(
                        "Poland",
                        "00-000",
                        "Warsaw",
                        "Street 10"
                ),
                firstMembershipPlan
        ));

        // Create the second membership with a member
        MembershipPlan secondMembershipPlan = membershipPlanRepository.save(new MembershipPlan(
                UUID.randomUUID(),
                "Plan2",
                MembershipType.PREMIUM,
                new Price(
                        new BigDecimal("99.99"),
                        Currency.getInstance("EUR")
                ),
                12,
                1,
                gym
        ));
        memberRepository.save(new Member(
                UUID.randomUUID(),
                "Jakub",
                "",
                "Nowak",
                "jakub.nowak@example.com",
                new Address(
                        "Poland",
                        "00-000",
                        "Warsaw",
                        "Street 20"
                ),
                secondMembershipPlan
        ));

        // Now there should be 1 gym, 2 membership plans and 2 members
        assertThat(gymRepository.count()).isEqualTo(1);
        assertThat(membershipPlanRepository.count()).isEqualTo(2);
        assertThat(memberRepository.count()).isEqualTo(2);

        // Getting a report with one record ("Gym", 124.98, "EUR")
        mockMvc.perform(get("/api/gyms/reports")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].gymName").value("Gym"))
                .andExpect(jsonPath("$[0].amount").value(124.98))
                .andExpect(jsonPath("$[0].currency").value("EUR"));
    }

    @Test
    void createReportForGymWithTwoMembershipPlansInDifferentCurrencies() throws Exception {
        // Create a gym
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

        // Create a membership plan with monthly price in EUR and a members
        MembershipPlan eurMembershipPlan = membershipPlanRepository.save(new MembershipPlan(
                UUID.randomUUID(),
                "Plan EUR",
                MembershipType.BASIC,
                new Price(
                        new BigDecimal("24.99"),
                        Currency.getInstance("EUR")
                ),
                12,
                2,
                gym
        ));
        memberRepository.save(new Member(
                UUID.randomUUID(),
                "Jan",
                "",
                "Kowalski",
                "jan.kowalski@example.com",
                new Address(
                        "Poland",
                        "00-000",
                        "Warsaw",
                        "Street 10"
                ),
                eurMembershipPlan
        ));

        // Create a membership with monthly price in PLN and a member
        MembershipPlan plnMembershipPlan = membershipPlanRepository.save(new MembershipPlan(
                UUID.randomUUID(),
                "Plan PLN",
                MembershipType.BASIC,
                new Price(
                        new BigDecimal("99.99"),
                        Currency.getInstance("PLN")
                ),
                12,
                1,
                gym
        ));
        memberRepository.save(new Member(
                UUID.randomUUID(),
                "Jakub",
                "",
                "Nowak",
                "jakub.nowak@example.com",
                new Address(
                        "Poland",
                        "00-000",
                        "Warsaw",
                        "Street 20"
                ),
                plnMembershipPlan
        ));

        // Now there should be 1 gym, 2 membership plans and 2 members
        assertThat(gymRepository.count()).isEqualTo(1);
        assertThat(membershipPlanRepository.count()).isEqualTo(2);
        assertThat(memberRepository.count()).isEqualTo(2);

        // Getting a report with 2 records
        mockMvc.perform(get("/api/gyms/reports")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].gymName").value("Gym"))
                .andExpect(jsonPath("$[0].amount").value(24.99))
                .andExpect(jsonPath("$[0].currency").value("EUR"))
                .andExpect(jsonPath("$[1].gymName").value("Gym"))
                .andExpect(jsonPath("$[1].amount").value(99.99))
                .andExpect(jsonPath("$[1].currency").value("PLN"));
    }

    @Test
    void createReportWithCancelledMember() throws Exception {
        // Create a gym
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

        // Create a membership plan
        MembershipPlan membershipPlan = membershipPlanRepository.save(new MembershipPlan(
                UUID.randomUUID(),
                "Plan PLN",
                MembershipType.BASIC,
                new Price(
                        new BigDecimal("24.99"),
                        Currency.getInstance("EUR")
                ),
                12,
                2,
                gym
        ));
        // Create a member
        Member memberToCancel = new Member(
                        UUID.randomUUID(),
                        "Jan",
                        "",
                        "Kowalski",
                        "jan.kowalski@example.com",
                        new Address(
                                "Poland",
                                "00-000",
                                "Warsaw",
                                "Street 10"
                        ),
                        membershipPlan
        );
        memberRepository.save(memberToCancel);

        // Set the status to CANCELLED
        mockMvc.perform(patch("/api/members/{id}", memberToCancel.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(memberToCancel.getId().toString()))
                .andExpect(jsonPath("$.status").value("CANCELLED"));

        // Create a new member
        memberRepository.save(new Member(
                UUID.randomUUID(),
                "Jan",
                "",
                "Kowalski",
                "jan.kowalski2@example.com",
                new Address(
                        "Poland",
                        "00-000",
                        "Warsaw",
                        "Street 10"
                ),
                membershipPlan
        ));

        // Now there should be 1 gym, 1 membership plan and 2 members
        assertThat(gymRepository.count()).isEqualTo(1);
        assertThat(membershipPlanRepository.count()).isEqualTo(1);
        assertThat(memberRepository.count()).isEqualTo(2);

        // Getting a report with 1 record ("Gym", 24.99, "EUR")
        mockMvc.perform(get("/api/gyms/reports")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].gymName").value("Gym"))
                .andExpect(jsonPath("$[0].amount").value(24.99))
                .andExpect(jsonPath("$[0].currency").value("EUR"));
    }


}
