package seg3x02.auctionsystem.domain.user.factories

import seg3x02.auctionsystem.adapters.dtos.CreditCardDto
import seg3x02.auctionsystem.domain.user.core.creditCard.CreditCard

interface CreditCardFactory {
    fun createCreditCard(creditCardInfo: CreditCardDto): CreditCard
}
