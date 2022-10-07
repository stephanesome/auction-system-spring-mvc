package seg3x02.auctionsystem.infrastructure.configuration

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import seg3x02.auctionsystem.application.services.AuctionScheduler
import seg3x02.auctionsystem.application.usecases.CloseAuction
import seg3x02.auctionsystem.domain.auction.facade.AuctionFacade
import seg3x02.auctionsystem.domain.auction.repositories.AuctionRepository
import java.time.LocalDateTime

@Component
class DbAuctionsSchedule(private val auctionRepository: AuctionRepository,
                         private val auctionFacade: AuctionFacade,
                         private val closeAuction: CloseAuction,
                         private val scheduler: AuctionScheduler): ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        val now = LocalDateTime.now()
        val auctions = auctionRepository.findActive()
        for (auction in auctions) {
            val closeTime = auctionFacade.getAuctionCloseTime(auction.id)
            if (closeTime != null) {
                if (closeTime.isAfter(now)) {
                    scheduler.scheduleClose(auction.id, closeTime)
                } else {
                    closeAuction.closeAuction(auction.id)
                }
            }
        }
    }
}
