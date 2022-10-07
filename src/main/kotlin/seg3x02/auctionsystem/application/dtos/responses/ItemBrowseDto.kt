package seg3x02.auctionsystem.application.dtos.responses

data class ItemBrowseDto(val title: String,
                    val description: String,
                    val image: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemBrowseDto

        if (title != other.title) return false
        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }

}
