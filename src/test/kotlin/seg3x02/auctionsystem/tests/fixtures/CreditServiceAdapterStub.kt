package seg3x02.auctionsystem.tests.fixtures

import seg3x02.auctionsystem.application.services.CreditService
import java.math.BigDecimal
import java.time.Month
import java.time.Year

class CreditServiceAdapterStub : CreditService {
    override fun processPayment(
        number: Number,
        expirationMonth: Month,
        expirationYear: Year,
        amt: BigDecimal
    ): Boolean {
        TODO("Not yet implemented")
    }

}
