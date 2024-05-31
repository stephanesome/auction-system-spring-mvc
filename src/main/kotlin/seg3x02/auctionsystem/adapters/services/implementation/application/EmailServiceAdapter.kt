package seg3x02.auctionsystem.adapters.services.implementation.application

import org.springframework.stereotype.Component
import seg3x02.auctionsystem.application.services.EmailService
import seg3x02.auctionsystem.infrastructure.services.EmailProvider

@Component
class EmailServiceAdapter(val provider: EmailProvider): EmailService {
    override fun sendSellerCompletedAuctionPaymentProcessedFine(email: String) {
        provider.sendEmail("auctionSystem@exa.com", email, "Payment Processed", "Completed Auction Payment Processed Fine")
        //println("CompletedAuctionPaymentProcessedFine to $email")
    }

    override fun sendWinnerCompletedAuctionEmailWithWinningBid(email: String) {
        provider.sendEmail("auctionSystem@exa.com", email, "Winning Bid", "Completed Auction Email With Winning Bid")
        // println("WinnerCompletedAuctionEmailWithWinningBid to $email")
    }

    override fun sendBidUnsuccessfulEmail(email: String) {
        provider.sendEmail("auctionSystem@exa.com", email, "Bid Error", "Bid Unsuccessful")
        // println("BidUnsuccessfulEmail to $email")
    }

    override fun sendSellerCompletedAuctionEmailNoWinningBid(email: String) {
        provider.sendEmail("auctionSystem@exa.com", email, "No Winning Bid", "Completed Auction No Winning Bid")
        // println("SellerCompletedAuctionEmailNoWinningBid to $email")
    }

    override fun sendSellerCompletedAuctionPaymentProcessFail(email: String) {
        provider.sendEmail("auctionSystem@exa.com", email, "Payment Processing Fail", "Completed Auction Payment Processing Fail")
        // println("SellerCompletedAuctionPaymentProcessFail to $email")
    }

    override fun sendAccountDeactivationEmail(email: String) {
        provider.sendEmail("auctionSystem@exa.com", email, "Account Deactivation", "Account Deactivation Email")
        // println("AccountDeactivationEmail to $email")
    }
}
