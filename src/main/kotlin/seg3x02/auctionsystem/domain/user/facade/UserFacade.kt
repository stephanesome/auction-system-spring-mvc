package seg3x02.auctionsystem.domain.user.facade

import seg3x02.auctionsystem.adapters.dtos.queries.AccountCreateDto
import seg3x02.auctionsystem.adapters.dtos.queries.CreditCardCreateDto
import seg3x02.auctionsystem.domain.user.core.account.PendingPayment
import seg3x02.auctionsystem.domain.user.core.account.UserAccount
import seg3x02.auctionsystem.domain.user.core.creditCard.CreditCard
import java.math.BigDecimal
import java.util.*

interface UserFacade {
    fun addCreditCard(userId: String, creditCardInfo: CreditCardCreateDto)
    fun hasPendingPayment(userId: String): Boolean
    fun getCreditCardNumber(userId: String): Number?
    fun addAuctionToSeller(userId: String, auctionId: UUID)
    fun createAccount(accountInfo: AccountCreateDto): Boolean
    fun updateAccount(userId: String, accountInfo: AccountCreateDto): Boolean
    fun getPendingPayment(userId: String): PendingPayment?
    fun removeUserPendingPayment(userId: String)
    fun getUserCreditCard(userId: String): CreditCard?
    fun addPendingPayment(userId: String, amt: BigDecimal)
    fun getUserEmailAddress(userId: String): String?
    fun deactivateAccount(userId: String): Boolean
    fun addBidToAccount(userId: String, bidId: UUID)
    fun getUserAuctions(userId: String): List<UUID>
    fun getUserAccount(userId: String): UserAccount?
}
