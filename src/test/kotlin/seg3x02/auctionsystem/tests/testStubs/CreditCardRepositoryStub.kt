package seg3x02.auctionsystem.tests.testStubs

import seg3x02.auctionsystem.domain.user.entities.creditCard.CreditCard
import seg3x02.auctionsystem.domain.user.repositories.CreditCardRepository
import kotlin.collections.HashMap

class CreditCardRepositoryStub : CreditCardRepository {
    private val cCards: MutableMap<String, CreditCard> = HashMap()

    override fun save(creditCard: CreditCard): CreditCard {
        cCards[creditCard.number] = creditCard
        return creditCard
    }

    override fun find(number: String): CreditCard? = cCards[number]
}
