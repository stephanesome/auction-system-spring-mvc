package seg3x02.auctionsystem.application.usecases

import seg3x02.auctionsystem.adapters.dtos.responses.AccountViewDto

interface ViewAccount {
    fun getAccount(userId: String): AccountViewDto?
}
