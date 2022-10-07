package seg3x02.auctionsystem.tests.testStubs

import seg3x02.auctionsystem.application.services.DomainEventEmitter
import seg3x02.auctionsystem.domain.common.DomainEvent
import seg3x02.auctionsystem.domain.auction.events.NewAuctionAdded
import seg3x02.auctionsystem.domain.auction.events.NewBidCreated
import seg3x02.auctionsystem.domain.user.events.*
import kotlin.collections.ArrayList

class EventEmitterAdapterStub : DomainEventEmitter {
    private val emitted: MutableList<DomainEvent> = ArrayList()

    override fun emit(event: DomainEvent) {
        emitted.add(event)
    }

    fun retrieveNewAuctionAddedEvent(): NewAuctionAdded? {
        return emitted.find { it is NewAuctionAdded} as NewAuctionAdded
    }

    fun retrieveNewBidCreatedEvent(): NewBidCreated? {
        return emitted.find { it is NewBidCreated} as NewBidCreated
    }

    fun retrieveUserAccountCreatedEvent(): UserAccountCreated {
        return emitted.find { it is UserAccountCreated} as UserAccountCreated
    }

    fun retrieveCreditCardCreatedEvent(): CreditCardCreated {
        return emitted.find { it is CreditCardCreated} as CreditCardCreated
    }

    fun retrieveUserCreditCardSetEvent(): UserCreditCardSet {
        return emitted.find { it is UserCreditCardSet} as UserCreditCardSet
    }

    fun retrieveUserAccountUpdatedEvent(): UserAccountUpdated {
        return emitted.find { it is UserAccountUpdated} as UserAccountUpdated
    }

    fun retrieveUserAccountDeactivatedEvent(): UserAccountDeactivated {
        return emitted.find { it is UserAccountDeactivated} as UserAccountDeactivated
    }
}
