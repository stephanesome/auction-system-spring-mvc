package seg3x02.auctionsystem.infrastructure.web.services

import org.mapstruct.factory.Mappers
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import seg3x02.auctionsystem.application.dtos.queries.BidCreateDto
import seg3x02.auctionsystem.application.dtos.responses.AccountViewDto
import seg3x02.auctionsystem.application.dtos.responses.AuctionBrowseDto
import seg3x02.auctionsystem.application.usecases.*
import seg3x02.auctionsystem.infrastructure.jpa.dao.UserJpaRepository
import seg3x02.auctionsystem.infrastructure.security.credentials.User
import seg3x02.auctionsystem.infrastructure.web.forms.AccountForm
import seg3x02.auctionsystem.infrastructure.web.forms.AuctionForm
import seg3x02.auctionsystem.infrastructure.web.forms.converters.AccountFormDtoConverter
import seg3x02.auctionsystem.infrastructure.web.forms.converters.AuctionFormDtoConverter
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Service
class AuctionService(
    private val browseAuctions: BrowseAuctions,
    private val viewAccount: ViewAccount,
    private val placeBid: PlaceBid,
    private val createAccount: CreateAccount,
    private val createAuction: CreateAuction,
    private val updateAccount: UpdateAccount,
    private val deactivateAccount: DeactivateAccount,
    private val userRepository: UserJpaRepository,
    private val encoder: PasswordEncoder
) {

    private val accountConverter = Mappers.getMapper(AccountFormDtoConverter::class.java)
    private val auctionConverter = Mappers.getMapper(AuctionFormDtoConverter::class.java)

    fun findAuctions(category: String): List<AuctionBrowseDto> {
        return browseAuctions.getAuctions(category)
    }

    fun createAccount(accountData: AccountForm): Boolean {
        return if (userRepository.existsByUsername(accountData.userName!!)) {
            false
        } else {
            val user = User(accountData.userName!!, encoder.encode(accountData.password), true)
            userRepository.save(user!!)
            // invoke create Account use
            val account = accountConverter.convertFormAccount(accountData)
            if (accountData.number != null && accountData.number!!.trim().isNotEmpty()) {
                val address = accountConverter.convertFormAddress(accountData)
                val cCard = accountConverter.convertFormCreditCard(accountData, address)
                account.creditCardInfo = cCard
            }
            return createAccount.createAccount(account)
        }
    }

    fun getAccount(userid: String): AccountViewDto? {
        return viewAccount.getAccount(userid)
    }

    fun createAuction(account: AccountViewDto, auctionData: AuctionForm): Boolean {
        val itemInfo = auctionConverter.convertItem(auctionData)
        itemInfo.image = auctionData.itemImageFile?.bytes
        val auction = auctionConverter.convertAuction(account.userName, auctionData, itemInfo)
        if (auctionData.number != null && auctionData.number!!.trim().isNotEmpty()) {
            val address = auctionConverter.convertAddress(auctionData)
            val cCard = auctionConverter.convertCreditCard(auctionData, address)
            auction.creditCardInfo = cCard
        }

        val aucId = createAuction.addAuction(auction)
        if (aucId != null) {
            val brAuc = browseAuctions.getAuctionBrowse(aucId)
            if (brAuc != null) {
                account.auctions.add(brAuc)
            }
        }
        return aucId != null
    }

    fun placeBid(userName: String, auctionId: UUID, amount: Double): Boolean {
        val bid = BidCreateDto(
            BigDecimal(amount),
            LocalDateTime.now(),
            userName
        )
        return placeBid.placeBid(userName, auctionId, bid) != null
    }

    fun setAccountForm(account: AccountViewDto): AccountForm {
        return accountConverter.convertDtoAccountView(account)
    }

    fun updateAccount(account: AccountViewDto, accountData: AccountForm): Boolean {
        // compare account with accountData
        val dataChange = accountDataChange(account, accountData)
        val ccChange = accountCreditCardChange(account, accountData)
        if (!dataChange && !ccChange) return true
        val accountDto = accountConverter.convertFormAccount(accountData)
        accountDto.creditCardInfo = null
        if (accountData.number != null &&
            accountData.number!!.isNotEmpty() &&
            ccChange
        ) {
            val address = accountConverter.convertFormAddress(accountData)
            val cCard = accountConverter.convertFormCreditCard(accountData, address)
            accountDto.creditCardInfo = cCard
        }
        return updateAccount.updateAccount(account.userName, accountDto)
    }

    fun deactivate(userName: String): Boolean {
        if (deactivateAccount.deactivateAccount(userName)) {
            val optUser = userRepository.findByUsername(userName)
            if (optUser.isPresent) {
                val user = optUser.get()
                user.enabled = false
                userRepository.save(user)
                return true
            }
        }
        return false
    }

    private fun accountDataChange(account: AccountViewDto, accountData: AccountForm): Boolean {
        return account.firstname != accountData.firstname ||
                account.lastname != accountData.lastname ||
                account.email != accountData.email
    }

    private fun accountCreditCardChange(account: AccountViewDto, accountData: AccountForm): Boolean {
        return account.creditCardNumber != accountData.number ||
                account.expirationMonth?.value != accountData.expirationMonth ||
                account.expirationYear?.value != accountData.expirationYear ||
                account.accountFirstname != accountData.accountFirstname ||
                account.accountLastname != accountData.accountLastname ||
                account.street != accountData.street ||
                account.city != accountData.city ||
                account.country != accountData.country ||
                account.postalCode != accountData.postalCode
    }
}
