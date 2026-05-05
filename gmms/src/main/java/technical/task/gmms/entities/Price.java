package technical.task.gmms.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

@Embeddable
public class Price {
    @Column(name = "price_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "price_currency", length = 3, nullable = false)
    private Currency currency;

    protected Price() {}

    public Price(BigDecimal amount, Currency currency) {
        if (amount == null || currency == null) {
            throw new IllegalArgumentException("Amount and currency must not be null");
        }
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Price price)) return false;
        return Objects.equals(getAmount(), price.getAmount()) && Objects.equals(getCurrency(), price.getCurrency());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAmount(), getCurrency());
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}
