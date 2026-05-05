package technical.task.gmms.dto;

import technical.task.gmms.entities.MembershipPlan;
import technical.task.gmms.entities.MembershipType;

import java.util.Objects;
import java.util.UUID;

public class MembershipPlanResponse {
    private UUID id;
    private String name;
    private MembershipType type;
    private String monthlyPrice;
    private Integer duration;
    private Integer maxMembers;
    private UUID gymId;

    public MembershipPlanResponse() {    }


    public MembershipPlanResponse(MembershipPlan membershipPlan) {
        this.id = membershipPlan.getId();
        this.name = membershipPlan.getName();
        this.type = membershipPlan.getType();
        this.monthlyPrice = membershipPlan.getMonthlyPrice().toString();
        this.duration = membershipPlan.getDuration();
        this.maxMembers = membershipPlan.getMaxMembers();
        this.gymId = membershipPlan.getGym().getId();
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

    public String getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(String monthlyPrice) {
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

    public void setMaxMembers(Integer maxMembers) {
        this.maxMembers = maxMembers;
    }

    public UUID getGymId() {
        return gymId;
    }

    public void setGymId(UUID gymId) {
        this.gymId = gymId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MembershipPlanResponse that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName()) && Objects.equals(getType(), that.getType()) && Objects.equals(getMonthlyPrice(), that.getMonthlyPrice()) && Objects.equals(getDuration(), that.getDuration()) && Objects.equals(getMaxMembers(), that.getMaxMembers()) && Objects.equals(getGymId(), that.getGymId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getType(), getMonthlyPrice(), getDuration(), getMaxMembers(), getGymId());
    }

    @Override
    public String toString() {
        return "MembershipPlanResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", monthlyPrice='" + monthlyPrice + '\'' +
                ", duration=" + duration +
                ", maxMembers=" + maxMembers +
                ", gymId=" + gymId +
                '}';
    }
}
