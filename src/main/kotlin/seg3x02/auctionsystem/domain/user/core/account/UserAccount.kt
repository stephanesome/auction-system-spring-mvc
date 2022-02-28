package seg3x02.auctionsystem.domain.user.core.account

import seg3x02.auctionsystem.application.services.CreditService
import seg3x02.auctionsystem.application.services.DomainEventEmitter
import seg3x02.auctionsystem.domain.user.core.creditCard.CreditCard
import seg3x02.auctionsystem.domain.user.events.UserCreditCardSet
import java.math.BigDecimal
import java.util.*

class UserAccount(
    val id: UUID,
    var firstname: String,
    var lastname: String,
    var email: String) {
    var creditCardNumber: Number? = null
    val auctions: MutableList<UUID> = ArrayList()
    var bids: MutableList<UUID> = ArrayList()
    var active: Boolean = true
    var pendingPayment: PendingPayment? = null

    fun addBid(bidId: UUID) {
        bids.add(bidId)
    }

    fun addPendingPayment(amt: BigDecimal) {
        pendingPayment = if (pendingPayment == null) {
            PendingPayment(amt)
        } else {
            val oldAmount = pendingPayment!!.amount
            PendingPayment(oldAmount.add(amt))
        }
    }

    fun setCreditCard(
        creditCard: CreditCard,
        eventEmitter: DomainEventEmitter,
        creditService: CreditService
    ) {
        this.creditCardNumber = creditCard.number
        val ccEvent = UserCreditCardSet(
            id = UUID.randomUUID(),
            occuredOn = Date(),
            creditCardNumber = creditCard.number,
            userId = this.id
        )
        eventEmitter.emit(ccEvent)
        val pending = this.pendingPayment
        if (pending != null) {
            if (creditService.processPayment(
                    creditCard.number,
                    creditCard.expirationMonth,
                    creditCard.expirationYear,
                    pending.amount)) {
                removePendingPayment()
            }
        }
    }

    fun update(updated: UserAccount) {
        firstname = updated.firstname
        lastname = updated.lastname
        email = updated.email
    }

    fun removePendingPayment() {
        pendingPayment = null
    }

    fun deactivate() {
        this.active = false
    }
}
