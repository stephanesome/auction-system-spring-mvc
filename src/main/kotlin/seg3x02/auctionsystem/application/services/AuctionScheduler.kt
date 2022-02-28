package seg3x02.auctionsystem.application.services

import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ScheduledFuture

interface AuctionScheduler {
    fun scheduleClose(auctionId: UUID, closeTime: LocalDateTime): ScheduledFuture<*>
}
