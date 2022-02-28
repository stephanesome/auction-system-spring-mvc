package seg3x02.auctionsystem.domain.auction.events

import seg3x02.auctionsystem.domain.DomainEvent
import java.util.*

class NewBidCreated(val id: UUID,
                    val occuredOn: Date,
                    val bidId: UUID): DomainEvent {
}
