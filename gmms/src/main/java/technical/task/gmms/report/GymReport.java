package technical.task.gmms.report;

import java.math.BigDecimal;
import java.util.Currency;

public record GymReport(
        String gymName,
        BigDecimal amount,
        Currency currency
) { }
