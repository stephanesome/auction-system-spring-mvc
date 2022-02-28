package seg3x02.auctionsystem.tests.fixtures

import seg3x02.auctionsystem.domain.user.core.creditCard.CreditCard
import seg3x02.auctionsystem.domain.user.repositories.CreditCardRepository
import kotlin.collections.HashMap

class CreditCardRepositoryStub : CreditCardRepository {
    private val cCards: MutableMap<Number, CreditCard> = HashMap()

    override fun save(creditCard: CreditCard): CreditCard {
        cCards[creditCard.number] = creditCard
        return creditCard
    }

    override fun find(number: Number): CreditCard? = cCards[number]
}
