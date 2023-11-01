package seg3x02.auctionsystem.infrastructure.jpa.entities.item

import java.util.*
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.Table

@Entity()
@Table(name="ITEMS")
data class ItemJpaEntity(@Id val id: UUID,
                    val title: String,
                    val description: String
) {
    @Lob
    var image: ByteArray? = null
}
