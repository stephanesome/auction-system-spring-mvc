package seg3x02.auctionsystem.tests.fixtures

import seg3x02.auctionsystem.application.services.AuctionScheduler
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.Future
import java.util.concurrent.FutureTask
import java.util.concurrent.ScheduledFuture

class AuctionSchedulerAdapterStub {
    fun scheduleClose(auctionId: UUID, closeTime: LocalDateTime) {

    }
}
