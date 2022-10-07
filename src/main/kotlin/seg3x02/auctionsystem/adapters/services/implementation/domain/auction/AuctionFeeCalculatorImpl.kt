package seg3x02.auctionsystem.adapters.services.implementation.domain.auction

import org.springframework.stereotype.Service
import seg3x02.auctionsystem.domain.auction.services.AuctionFeeCalculator
import java.math.BigDecimal
import java.util.*

@Service
class AuctionFeeCalculatorImpl: AuctionFeeCalculator {
    override fun getAuctionFee(auctionId: UUID): BigDecimal {
        // basic default implementation
        return BigDecimal(0)
    }
}
