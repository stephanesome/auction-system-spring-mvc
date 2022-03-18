package seg3x02.auctionsystem.framework.web.services

import org.mapstruct.factory.Mappers
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import seg3x02.auctionsystem.adapters.dtos.responses.AccountViewDto
import seg3x02.auctionsystem.adapters.dtos.responses.AuctionBrowseDto
import seg3x02.auctionsystem.application.usecases.BrowseAuctions
import seg3x02.auctionsystem.application.usecases.CreateAccount
import seg3x02.auctionsystem.application.usecases.CreateAuction
import seg3x02.auctionsystem.application.usecases.ViewAccount
import seg3x02.auctionsystem.framework.jpa.dao.UserJpaRepository
import seg3x02.auctionsystem.framework.security.credentials.User
import seg3x02.auctionsystem.framework.web.forms.AccountForm
import seg3x02.auctionsystem.framework.web.forms.AuctionForm
import seg3x02.auctionsystem.framework.web.forms.converters.AccountFormDtoConverter
import seg3x02.auctionsystem.framework.web.forms.converters.AuctionFormDtoConverter

@Service
class AuctionService(private val browseAuctions: BrowseAuctions,
                     private val viewAccount: ViewAccount,
                     private val createAccount: CreateAccount,
                     private val createAuction: CreateAuction,
                     private val userRepository: UserJpaRepository,
                     private val encoder: PasswordEncoder) {

    private val accountConverter = Mappers.getMapper(AccountFormDtoConverter::class.java)
    private val auctionConverter = Mappers.getMapper(AuctionFormDtoConverter::class.java)

    fun findAuctions(category: String): List<AuctionBrowseDto> {
        return browseAuctions.getAuctions(category)
    }

    fun createAccount(accountData: AccountForm): Boolean {
        return if (userRepository.existsByUsername(accountData.userName!!))  {
            false
        } else {
            val user = User(accountData.userName!!, encoder.encode(accountData.password))
            userRepository.save(user!!)
            // invoke create Account use
            val account = accountConverter.convertAccount(accountData)
            if ((accountData.number != null) &&
                (accountData.number != 0L)
            ) {
                val address = accountConverter.convertAddress(accountData)
                val cCard = accountConverter.convertCreditCard(accountData, address)
                account.creditCardInfo = cCard
            }
            return createAccount.createAccount(account)
        }
    }

    fun getAccount(userid: String): AccountViewDto? {
        return viewAccount.getAccount(userid)
    }

    fun createAuction(account: AccountViewDto, auctionData: AuctionForm): Boolean {
        return if (account.pendingPayment > 0.0 ||
            account.creditCardNumber == null) {
            false
        } else {
            val auction = auctionConverter.convertAuction(account.userName, auctionData)
            auction.itemInfo = auctionConverter.convertItem(auctionData)
            auction.itemInfo.image = auctionData.itemImageFile?.bytes
            val aucId = createAuction.addAuction(auction)
            aucId != null
        }
    }

}
