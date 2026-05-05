package technical.task.gmms.entities;

import jakarta.persistence.*;
import technical.task.gmms.exceptions.MembershipCapacityExceededException;

import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name="membership_plans")
public class MembershipPlan {
    @Id
    private UUID id;

    @Column(name = "membership_name")
    private String name;

    @Enumerated(EnumType.STRING)
    private MembershipType type;

    @Embedded
    private Price monthlyPrice;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private Integer maxMembers;

    @ManyToOne
    private Gym gym;

    @OneToMany(
            mappedBy = "membership",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Member> members = new ArrayList<>();

    public MembershipPlan() {    }

    public MembershipPlan(UUID id, String name, MembershipType type, Price monthlyPrice, Integer duration, Integer maxMembers, Gym gym) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.monthlyPrice = monthlyPrice;
        this.duration = duration;
        this.maxMembers = maxMembers;
        this.gym = gym;
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

    public MembershipType getType() {
        return type;
    }

    public void setType(MembershipType type) {
        this.type = type;
    }

    public Price getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(Price monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(Integer maxMembers) throws IllegalArgumentException{
        if (members.size() > maxMembers)
            throw new IllegalArgumentException("Cannot set max members to " + maxMembers +
                    ", because there are already " + members.size() + " members.");
        this.maxMembers = maxMembers;
    }

    public Gym getGym() {
        return gym;
    }

    public void setGym(Gym gym) {
        this.gym = gym;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public void addMember(Member member) throws MembershipCapacityExceededException {
        if (members.size() >= maxMembers)
            throw new MembershipCapacityExceededException("Membership plan has reached its maximum capacity of " + maxMembers);
        members.add(member);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MembershipPlan that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName()) && getType() == that.getType() && Objects.equals(getMonthlyPrice(), that.getMonthlyPrice()) && Objects.equals(getDuration(), that.getDuration()) && Objects.equals(getMaxMembers(), that.getMaxMembers()) && Objects.equals(getGym(), that.getGym()) && Objects.equals(getMembers(), that.getMembers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getType(), getMonthlyPrice(), getDuration(), getMaxMembers(), getGym(), getMembers());
    }

    @Override
    public String toString() {
        return "MembershipPlan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", monthlyPrice=" + monthlyPrice +
                ", duration=" + duration +
                ", maxMembers=" + maxMembers +
                ", gym=" + gym +
                ", members=" + members +
                '}';
    }
}
