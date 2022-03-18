package seg3x02.auctionsystem.framework.jpa.entities.user.account

import java.util.*
import javax.persistence.*

@Entity()
@Table(name="ACCOUNTS")
class UserAccountJpaEntity(@Id val id: String,
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
