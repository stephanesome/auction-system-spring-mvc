package seg3x02.auctionsystem.domain.user.facade.implementation

import seg3x02.auctionsystem.adapters.dtos.AccountDto
import seg3x02.auctionsystem.adapters.dtos.CreditCardDto
import seg3x02.auctionsystem.application.services.CreditService
import seg3x02.auctionsystem.application.services.DomainEventEmitter
import seg3x02.auctionsystem.domain.auction.events.NewAuctionBidRegistered
import seg3x02.auctionsystem.domain.user.core.account.PendingPayment
import seg3x02.auctionsystem.domain.user.core.creditCard.CreditCard
import seg3x02.auctionsystem.domain.user.events.CreditCardCreated
import seg3x02.auctionsystem.domain.user.events.UserAccountCreated
import seg3x02.auctionsystem.domain.user.events.UserAccountDeactivated
import seg3x02.auctionsystem.domain.user.events.UserAccountUpdated
import seg3x02.auctionsystem.domain.user.facade.UserFacade
import seg3x02.auctionsystem.domain.user.factories.AccountFactory
import seg3x02.auctionsystem.domain.user.factories.CreditCardFactory
import seg3x02.auctionsystem.domain.user.repositories.CreditCardRepository
import seg3x02.auctionsystem.domain.user.repositories.UserRepository
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

class UserFacadeImpl(private val userRepository: UserRepository,
                     private val accountFactory: AccountFactory,
                     private val creditCardRepository: CreditCardRepository,
                     private var creditCardFactory: CreditCardFactory,
                     private var eventEmitter: DomainEventEmitter,
                     private var creditService: CreditService
): UserFacade {

    override fun addCreditCard(userId: UUID, creditCardInfo: CreditCardDto) {
        val creditCard = createCreditCard(creditCardInfo)
        val user = userRepository.find(userId)
        user?.setCreditCard(creditCard, eventEmitter, creditService)
    }

    private fun createCreditCard(creditCardInfo: CreditCardDto): CreditCard {
        val creditCard = creditCardFactory.createCreditCard(creditCardInfo)
        creditCardRepository.save(creditCard)
        eventEmitter.emit(CreditCardCreated(UUID.randomUUID(), Date(), creditCard.number))
        return creditCard
    }

    override fun hasPendingPayment(userId: UUID): Boolean {
        val user = userRepository.find(userId)
        return if (user == null) false else user.pendingPayment != null
    }

    override fun getCreditCardNumber(userId: UUID): Number? {
        return userRepository.find(userId)?.creditCardNumber
    }

    override fun getUserCreditCard(userId: UUID): CreditCard? {
        val ccNumber = this.getCreditCardNumber(userId)
        return if (ccNumber != null) {
            creditCardRepository.find(ccNumber)
        } else null
    }

    override fun addAuctionToSeller(userId: UUID, auctionId: UUID) {
        userRepository.find(userId)?.auctions?.add(auctionId)
    }

    override fun createAccount(accountInfo: AccountDto): UUID? {
        val userAccount = accountFactory.createAccount(accountInfo)
        val ccInfo = accountInfo.creditCardInfo
        if (ccInfo != null) {
            val ccCard = createCreditCard(ccInfo)
            userAccount.setCreditCard(ccCard, eventEmitter, creditService)
        }
        userRepository.save(userAccount)
        eventEmitter.emit(UserAccountCreated(UUID.randomUUID(),
                                    Date(),
                                    userAccount.id))
        return userAccount.id
    }

    override fun updateAccount(userId: UUID, accountInfo: AccountDto): Boolean {
        val user = userRepository.find(userId)
        if (user != null) {
            val updated = accountFactory.createAccount(accountInfo)
            user.update(updated)
            val ccInfo = accountInfo.creditCardInfo
            if (ccInfo != null) {
                val newCard: CreditCard = createCreditCard(ccInfo)
                user.setCreditCard(newCard, eventEmitter, creditService)
            }
            userRepository.save(user)
            eventEmitter.emit(UserAccountUpdated(UUID.randomUUID(),
                                                    Date(),
                                                    user.id))
            return true
        }
        return false
    }

    override fun getPendingPayment(userId: UUID): PendingPayment? {
        val user = userRepository.find(userId)
        return user?.pendingPayment
    }

    override fun removeUserPendingPayment(userId: UUID) {
        val user = userRepository.find(userId)
        user?.removePendingPayment()
    }

    override fun addPendingPayment(userId: UUID, amt: BigDecimal) {
        val user = userRepository.find(userId)
        user?.addPendingPayment(amt)
    }

    override fun getUserEmailAddress(userId: UUID): String? {
        return userRepository.find(userId)?.email
    }

    override fun deactivateAccount(userId: UUID): Boolean {
        val user = userRepository.find(userId)
        if (user != null) {
            user.deactivate()
            eventEmitter.emit(UserAccountDeactivated(
                UUID.randomUUID(),
                Date(),
                user.id
            ))
            return true
        }
        return false
    }

    override fun addBidToAccount(userId: UUID, bidId: UUID) {
        userRepository.find(userId)?.addBid(bidId)
        val addBidEvent = NewAuctionBidRegistered(
            UUID.randomUUID(),
            Date(),
            bidId,
            userId)
        eventEmitter.emit(addBidEvent)
    }

    override fun getUserAuctions(userId: UUID): List<UUID> {
        val auctionList = ArrayList<UUID>()
        val user = userRepository.find(userId)
        if (user != null) {
            auctionList.addAll(user.auctions)
        }
        return auctionList
    }
}
