package seg3x02.auctionsystem.adapters.repositories.converters

import org.mapstruct.Mapper
import seg3x02.auctionsystem.domain.user.entities.account.UserAccount
import seg3x02.auctionsystem.framework.jpa.entities.user.account.UserAccountJpaEntity

@Mapper
interface AccountJpaConverter {
    fun convertToJpa(account: UserAccount): UserAccountJpaEntity

    fun convertToModel(accountJpa: UserAccountJpaEntity): UserAccount
}
