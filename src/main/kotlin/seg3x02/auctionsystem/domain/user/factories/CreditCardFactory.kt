package seg3x02.auctionsystem.domain.user.factories

import seg3x02.auctionsystem.adapters.dtos.queries.CreditCardCreateDto
import seg3x02.auctionsystem.domain.user.core.creditCard.CreditCard

interface CreditCardFactory {
    fun createCreditCard(creditCardInfo: CreditCardCreateDto): CreditCard
}
