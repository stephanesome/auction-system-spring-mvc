package seg3x02.auctionsystem.adapters.dtos.responses

class AccountViewDto(
    val userName: String,
    val firstname: String,
    val lastname: String,
    val email: String,
    val pendingPayment: Double,
    val auctions: List<AuctionBrowseDto>,
    var creditCardNumber: Number? = null) {
}
