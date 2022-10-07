package seg3x02.auctionsystem.infrastructure.jpa.entities.user.account

import java.util.*
import javax.persistence.*

@Entity()
@Table(name="ACCOUNTS")
class UserAccountJpaEntity(@Id val id: String,
                           val firstname: String,
                           val lastname: String,
                           val email: String
    ) {
    var creditCardNumber: String? = null
    @ElementCollection
    @CollectionTable(name="ACCOUNTS_AUCTIONS",
            joinColumns= [JoinColumn(name = "ACCOUNT_ID")])
    val auctions: MutableList<UUID> = ArrayList()
    @ElementCollection
    @CollectionTable(name="ACCOUNTS_BIDS",
        joinColumns= [JoinColumn(name = "ACCOUNT_ID")])
    val bids: MutableList<UUID> = ArrayList()
    var active: Boolean = true
    @Embedded
    var pendingPayment: PendingPaymentJpaEntity? = null
}

