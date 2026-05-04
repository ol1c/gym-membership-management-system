package technical.task.gmms.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name="gyms")
public class Gym {
    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    private Address address;

    @OneToMany(
            mappedBy = "gym",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<MembershipPlan> membershipPlans = new ArrayList<>();

    public Gym() {    }

    public Gym(UUID id, String name, String phoneNumber, Address address) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<MembershipPlan> getMembershipPlans() {
        return membershipPlans;
    }

    public void setMembershipPlans(List<MembershipPlan> membershipPlans) {
        this.membershipPlans = membershipPlans;
    }

    public void addMembershipPlan(MembershipPlan membershipPlan) {
        this.membershipPlans.add(membershipPlan);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Gym gym)) return false;
        return Objects.equals(getId(), gym.getId()) && Objects.equals(getName(), gym.getName()) && Objects.equals(getPhoneNumber(), gym.getPhoneNumber()) && Objects.equals(getAddress(), gym.getAddress()) && Objects.equals(getMembershipPlans(), gym.getMembershipPlans());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPhoneNumber(), getAddress(), getMembershipPlans());
    }

    @Override
    public String toString() {
        return "Gym{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address=" + address +
                ", membershipPlans=" + membershipPlans +
                '}';
    }
}
