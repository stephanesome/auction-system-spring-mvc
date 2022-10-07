package seg3x02.auctionsystem.infrastructure.jpa.entities.item

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.Table

@Entity()
@Table(name="ITEMS")
data class ItemJpaEntity(@Id val id: UUID,
                    val title: String,
                    val description: String
) {
    @Lob
    var image: ByteArray? = null
}
