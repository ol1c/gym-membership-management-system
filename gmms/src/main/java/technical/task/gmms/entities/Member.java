package technical.task.gmms.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name="members")
public class Member {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    private String secondName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private Address address;

    @ManyToOne
    private MembershipPlan membership;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    private MembershipStatus status = MembershipStatus.ACTIVE;

    public Member() {    }


    public Member(UUID id, String firstName, String secondName, String lastName, String email, Address address, MembershipPlan membership) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.membership = membership;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public MembershipPlan getMembership() {
        return membership;
    }

    public void setMembership(MembershipPlan membership) {
        this.membership = membership;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public MembershipStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Member member)) return false;
        return Objects.equals(getId(), member.getId()) && Objects.equals(getFirstName(), member.getFirstName()) && Objects.equals(getSecondName(), member.getSecondName()) && Objects.equals(getLastName(), member.getLastName()) && Objects.equals(getEmail(), member.getEmail()) && Objects.equals(getAddress(), member.getAddress()) && Objects.equals(getMembership(), member.getMembership()) && Objects.equals(getStartDate(), member.getStartDate()) && getStatus() == member.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getSecondName(), getLastName(), getEmail(), getAddress(), getMembership(), getStartDate(), getStatus());
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", address=" + address +
                ", membership=" + membership +
                ", startDate=" + startDate +
                ", status=" + status +
                '}';
    }
}
