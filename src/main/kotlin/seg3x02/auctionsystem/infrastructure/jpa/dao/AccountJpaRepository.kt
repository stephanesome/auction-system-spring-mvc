package seg3x02.auctionsystem.infrastructure.jpa.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import seg3x02.auctionsystem.infrastructure.jpa.entities.user.account.UserAccountJpaEntity

@Repository
interface AccountJpaRepository: CrudRepository<UserAccountJpaEntity, String> {
}
