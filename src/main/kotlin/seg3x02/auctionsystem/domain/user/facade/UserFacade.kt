package seg3x02.auctionsystem.domain.user.facade

import seg3x02.auctionsystem.adapters.dtos.AccountDto
import seg3x02.auctionsystem.adapters.dtos.CreditCardDto
import seg3x02.auctionsystem.domain.user.core.account.PendingPayment
import seg3x02.auctionsystem.domain.user.core.creditCard.CreditCard
import java.math.BigDecimal
import java.util.*

interface UserFacade {
    fun addCreditCard(userId: UUID, creditCardInfo: CreditCardDto)
    fun hasPendingPayment(userId: UUID): Boolean
    fun getCreditCardNumber(userId: UUID): Number?
    fun addAuctionToSeller(userId: UUID, auctionId: UUID)
    fun createAccount(accountInfo: AccountDto): UUID?
    fun updateAccount(userId: UUID, accountInfo: AccountDto): Boolean
    fun getPendingPayment(userId: UUID): PendingPayment?
    fun removeUserPendingPayment(userId: UUID)
    fun getUserCreditCard(userId: UUID): CreditCard?
    fun addPendingPayment(userId: UUID, amt: BigDecimal)
    fun getUserEmailAddress(userId: UUID): String?
    fun deactivateAccount(userId: UUID): Boolean
    fun addBidToAccount(userId: UUID, bidId: UUID)
    fun getUserAuctions(userId: UUID): List<UUID>
}
