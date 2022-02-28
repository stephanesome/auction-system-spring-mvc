package seg3x02.auctionsystem.application.usecases.implementation

import seg3x02.auctionsystem.adapters.dtos.AccountDto
import seg3x02.auctionsystem.application.services.CreditService
import seg3x02.auctionsystem.application.usecases.UpdateAccount
import seg3x02.auctionsystem.domain.user.facade.UserFacade
import java.util.*

class UpdateAccountImpl(
    private var userFacade: UserFacade): UpdateAccount {

    override fun updateAccount(accountId: UUID, accountInfo: AccountDto): Boolean {
        return userFacade.updateAccount(accountId, accountInfo)
    }
}
