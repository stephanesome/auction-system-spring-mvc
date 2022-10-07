package seg3x02.auctionsystem.adapters.services.implementation.application

import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Component
import seg3x02.auctionsystem.application.services.AuctionScheduler
import seg3x02.auctionsystem.application.usecases.CloseAuction
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import java.util.concurrent.ScheduledFuture

@Component
class AuctionSchedulerAdapter(private val taskScheduler: TaskScheduler,
            val closeAuction: CloseAuction): AuctionScheduler {
    override fun scheduleClose(auctionId: UUID, closeTime: LocalDateTime): ScheduledFuture<*> {
        val closing =  Date
            .from(closeTime.atZone(ZoneId.systemDefault())
                .toInstant())
        return taskScheduler.schedule(
            CloseAuctionRunnableTask(auctionId),
            closing
        )
    }

    inner class CloseAuctionRunnableTask(private val auctionId: UUID): Runnable {
        override fun run() {
            closeAuction.closeAuction(auctionId)
        }
    }
}

