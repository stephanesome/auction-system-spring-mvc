package seg3x02.auctionsystem.framework.jpa.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import seg3x02.auctionsystem.framework.jpa.entities.user.account.UserAccountJpaEntity
import java.util.*

@Repository
interface AccountJpaRepository: CrudRepository<UserAccountJpaEntity, String> {
}
