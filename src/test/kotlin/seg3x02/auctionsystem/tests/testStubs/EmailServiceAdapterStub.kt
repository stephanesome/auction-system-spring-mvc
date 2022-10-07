package seg3x02.auctionsystem.tests.testStubs

import seg3x02.auctionsystem.application.services.EmailService

class EmailServiceAdapterStub : EmailService {
    override fun sendSellerCompletedAuctionPaymentProcessedFine(sellerEmailAddress: String) {
        TODO("Not yet implemented")
    }

    override fun sendWinnerCompletedAuctionEmailWithWinningBid(winnerEmailAddress: String) {
        TODO("Not yet implemented")
    }

    override fun sendBidUnsuccessfulEmail(emailAddress: String) {
        TODO("Not yet implemented")
    }

    override fun sendSellerCompletedAuctionEmailNoWinningBid(emailAddress: String) {
        TODO("Not yet implemented")
    }

    override fun sendSellerCompletedAuctionPaymentProcessFail(emailAddress: String) {
        TODO("Not yet implemented")
    }

    override fun sendAccountDeactivationEmail(email: String) {
        TODO("Not yet implemented")
    }

}
