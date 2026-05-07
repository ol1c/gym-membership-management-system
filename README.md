# Gym Membership Management System
> A project created for the needs of a Technical Task (SII - Letnia Akademia Talentów 2026)

## Project Overview
Gym Membership Management System (GMMS) is an application for managing gym memberships, including gyms, membership plans
and members. GMMS is a [Spring Boot](https://spring.io/guides/gs/spring-boot) application built using [Maven](https://spring.io/guides/gs/maven/). It uses an H2 in-memory database for data storage.

## Run GMMS locally
Java 21 or later is required for the build, and the application can run with Java 21 or newer.

1. Clone the project locally:
```bash
git clone https://github.com/ol1c/gym-membership-management-system.git
cd gym-membership-management-system/gmms
```

2. Start the application on the command line:

```bash
./mvnw spring-boot:run
```
You can access the GMMS at http://localhost:8080/.

## Required Endpoints
To test the required endpoints, you can use the `request.http` file located in the `gmms/` directory.

1. **Create a new gym**

   `POST http://localhost:8080/api/gyms`
2. **List all gyms**

   `GET http://localhost:8080/api/gyms`
3. **Create a new membership plan for a given gym**

   `POST http://localhost:8080/api/membership-plans/gyms/{gymId}`
4. **List all membership plans for a given gym**

   `GET http://localhost:8080/api/membership-plans/gyms/{gymId}`
5. **Register a new member to a given membership plan** (validates capacity)

   `POST http://localhost:8080/api/members/membership-plans/{membershipPlanId}`
6. **List all members** (includes the plan name, gym name, and status)

   `GET http://localhost:8080/api/members`
7. **Cancel a membership**

   `PATCH http://localhost:8080/api/members/{memberId}`

## Tests
Tests have been created covering all endpoint functionalities:
- `GymTest.java`
- `MembershipPlanTest.java`
- `MemberTest.java`

To run the tests, execute the following Maven command in your terminal:
```bash
./mvnw test
```

## Optional task

The following additional task has been implemented:
> **[Optional] Revenue report:** display total monthly revenue per gym, grouped by currency.
> Monthly revenue = sum of monthly prices of all ACTIVE members for each gym.

Tests for the report functionality can be found in `ReportTest.java`.

**REST API Endpoint:**
Generate a report of monthly revenue for all gyms
`GET http://localhost:8080/api/gyms/reports`

*(This request is also available in the `gmms/request.http` file)*

