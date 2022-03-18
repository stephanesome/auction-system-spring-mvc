package seg3x02.auctionsystem.application.usecases.implementation

import seg3x02.auctionsystem.adapters.dtos.queries.AccountCreateDto
import seg3x02.auctionsystem.application.usecases.CreateAccount
import seg3x02.auctionsystem.domain.user.facade.UserFacade
import java.util.*

class CreateAccountImpl(private var userFacade: UserFacade): CreateAccount {
    override fun createAccount(accountInfo: AccountCreateDto): Boolean {
        return userFacade.createAccount(accountInfo)
    }
}
