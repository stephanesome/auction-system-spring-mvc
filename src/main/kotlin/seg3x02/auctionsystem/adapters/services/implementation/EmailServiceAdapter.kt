package seg3x02.auctionsystem.adapters.services.implementation

import org.springframework.stereotype.Component
import seg3x02.auctionsystem.application.services.EmailService

@Component
class EmailServiceAdapter: EmailService {
    override fun sendSellerCompletedAuctionPaymentProcessedFine(email: String) {
        println("CompletedAuctionPaymentProcessedFine to $email")
    }

    override fun sendWinnerCompletedAuctionEmailWithWinningBid(email: String) {
        println("WinnerCompletedAuctionEmailWithWinningBid to $email")
    }

    override fun sendBidUnsuccessfulEmail(email: String) {
        println("BidUnsuccessfulEmail to $email")
    }

    override fun sendSellerCompletedAuctionEmailNoWinningBid(email: String) {
        println("SellerCompletedAuctionEmailNoWinningBid to $email")
    }

    override fun sendSellerCompletedAuctionPaymentProcessFail(email: String) {
        println("SellerCompletedAuctionPaymentProcessFail to $email")
    }

    override fun sendAccountDeactivationEmail(email: String) {
        println("AccountDeactivationEmail to $email")
    }
}
