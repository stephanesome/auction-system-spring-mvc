package seg3x02.auctionsystem.adapters.dtos.queries

class ItemCreateDto(val title: String,
                    val description: String,
                    var image: ByteArray? = null) {
}
