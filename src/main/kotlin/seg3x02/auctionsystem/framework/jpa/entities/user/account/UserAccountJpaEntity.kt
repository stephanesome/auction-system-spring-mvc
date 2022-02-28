package seg3x02.auctionsystem.framework.jpa.entities.user.account

import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import java.util.*
import javax.persistence.*

@Entity()
@Table(name="USERS")
class UserAccountJpaEntity(@Id val id: UUID,
                           val firstname: String,
                           val lastname: String,
                           val email: String
    ) {
    var creditCardNumber: Number? = null
    @ElementCollection
    val auctions: MutableList<UUID> = ArrayList()
    @ElementCollection
    val bids: MutableList<UUID> = ArrayList()
    var active: Boolean = true
    @Embedded
    var pendingPayment: PendingPaymentJpaEntity? = null
}
