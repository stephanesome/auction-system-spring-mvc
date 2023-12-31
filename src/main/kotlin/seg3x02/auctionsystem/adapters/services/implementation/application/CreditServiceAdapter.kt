package seg3x02.auctionsystem.adapters.services.implementation.application

import org.springframework.stereotype.Component
import seg3x02.auctionsystem.application.services.CreditService
import java.math.BigDecimal
import java.time.Month
import java.time.Year

@Component
class CreditServiceAdapter: CreditService {
    override fun processPayment(
        number: String,
        expirationMonth: Month,
        expirationYear: Year,
        amt: BigDecimal
    ): Boolean {
        return true
    }
}
