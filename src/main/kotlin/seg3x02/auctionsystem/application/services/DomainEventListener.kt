package seg3x02.auctionsystem.application.services

import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import seg3x02.auctionsystem.domain.auction.events.AuctionClosed
import seg3x02.auctionsystem.domain.auction.events.NewAuctionAdded
import seg3x02.auctionsystem.domain.auction.events.NewAuctionBidRegistered
import seg3x02.auctionsystem.domain.auction.events.NewBidCreated
import seg3x02.auctionsystem.domain.item.events.NewItemAdded
import seg3x02.auctionsystem.domain.user.events.*

interface DomainEventListener {
    fun handleNewItemAddedEvent(event: NewItemAdded)
    @Async
    @EventListener
    fun handleCreditCardCreatedEvent(event: CreditCardCreated)
    @Async
    @EventListener
    fun handleUserAccountCreatedEvent(event: UserAccountCreated)
    @Async
    @EventListener
    fun handleUserAccountDeactivatedEvent(event: UserAccountDeactivated)
    @Async
    @EventListener
    fun handleUserAccountUpdatedEvent(event: UserAccountUpdated)
    @Async
    @EventListener
    fun handleUserCreditCardSetEvent(event: UserCreditCardSet)
    @Async
    @EventListener
    fun handleAuctionClosedEvent(event: AuctionClosed)
    @Async
    @EventListener
    fun handleNewAuctionAddedEvent(event: NewAuctionAdded)
    @Async
    @EventListener
    fun handleNewAuctionBidRegisteredEvent(event: NewAuctionBidRegistered)
    @Async
    @EventListener
    fun handleNewBidCreatedEvent(event: NewBidCreated)
}
