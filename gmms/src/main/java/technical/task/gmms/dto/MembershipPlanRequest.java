package technical.task.gmms.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import technical.task.gmms.entities.MembershipType;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public class MembershipPlanRequest {
    @NotBlank
    private String name;

    @NotNull
    private MembershipType type;

    @NotNull
    @Positive
    @Digits(integer = 10, fraction = 2)
    private BigDecimal monthlyPriceAmount;

    @NotNull
    private Currency monthlyPriceCurrency;

    @NotNull
    private Integer duration;

    @NotNull
    private Integer maxMembers;

    public MembershipPlanRequest() {    }


    public MembershipPlanRequest(String name, MembershipType type, BigDecimal monthlyPriceAmount, Currency monthlyPriceCurrency, Integer duration, Integer maxMembers) {
        this.name = name;
        this.type = type;
        this.monthlyPriceAmount = monthlyPriceAmount;
        this.monthlyPriceCurrency = monthlyPriceCurrency;
        this.duration = duration;
        this.maxMembers = maxMembers;
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

    public BigDecimal getMonthlyPriceAmount() {
        return monthlyPriceAmount;
    }

    public void setMonthlyPriceAmount(BigDecimal monthlyPriceAmount) {
        this.monthlyPriceAmount = monthlyPriceAmount;
    }

    public Currency getMonthlyPriceCurrency() {
        return monthlyPriceCurrency;
    }

    public void setMonthlyPriceCurrency(Currency monthlyPriceCurrency) {
        this.monthlyPriceCurrency = monthlyPriceCurrency;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MembershipPlanRequest that)) return false;
        return Objects.equals(getName(), that.getName()) && getType() == that.getType() && Objects.equals(getMonthlyPriceAmount(), that.getMonthlyPriceAmount()) && Objects.equals(getMonthlyPriceCurrency(), that.getMonthlyPriceCurrency()) && Objects.equals(getDuration(), that.getDuration()) && Objects.equals(getMaxMembers(), that.getMaxMembers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getType(), getMonthlyPriceAmount(), getMonthlyPriceCurrency(), getDuration(), getMaxMembers());
    }

    @Override
    public String toString() {
        return "MembershipPlanRequest{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", monthlyPriceAmount=" + monthlyPriceAmount +
                ", monthlyPriceCurrency=" + monthlyPriceCurrency +
                ", duration=" + duration +
                ", maxMembers=" + maxMembers +
                '}';
    }
}
