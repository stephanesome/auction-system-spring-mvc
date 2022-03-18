package seg3x02.auctionsystem.application.usecases.implementation

import org.mapstruct.factory.Mappers
import org.springframework.beans.factory.annotation.Autowired
import seg3x02.auctionsystem.adapters.dtos.queries.converters.AccountViewConverter
import seg3x02.auctionsystem.adapters.dtos.responses.AccountViewDto
import seg3x02.auctionsystem.application.usecases.BrowseAuctions
import seg3x02.auctionsystem.application.usecases.ViewAccount
import seg3x02.auctionsystem.domain.user.facade.UserFacade

class ViewAccountImpl(private val userFacade: UserFacade): ViewAccount {

    @Autowired
    private lateinit var browseAuctions: BrowseAuctions

    private val accountConverter = Mappers.getMapper(AccountViewConverter::class.java)

    override fun getAccount(userId: String): AccountViewDto? {
        val account = userFacade.getUserAccount(userId)
        return if (account != null && account.active) {
            val auctions = browseAuctions.getUserAuctions(account)
            accountConverter.convertToView(account, auctions)
        } else {
            null
        }
    }
}
