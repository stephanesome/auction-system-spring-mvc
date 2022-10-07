package seg3x02.auctionsystem.adapters.repositories

import org.mapstruct.factory.Mappers
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import seg3x02.auctionsystem.adapters.repositories.converters.ItemJpaConverter
import seg3x02.auctionsystem.domain.item.entities.Item
import seg3x02.auctionsystem.domain.item.repositories.ItemRepository
import seg3x02.auctionsystem.infrastructure.jpa.dao.ItemJpaRepository
import java.util.*

@Component
@CacheConfig(cacheNames=["items"])
class ItemJpaAdapter(private val itemRepository: ItemJpaRepository): ItemRepository {
    private val converter = Mappers.getMapper(ItemJpaConverter::class.java)

   @Cacheable(key = "#id")
    override fun find(id: UUID): Item? {
        val itemJpa = itemRepository.findByIdOrNull(id)
        return itemJpa?.let { converter.convertToModel(it) }
    }

   @CachePut(key = "#item.id")
    override fun save(item: Item): Item {
        val itemJpa = converter.convertToJpa(item)
        itemRepository.save(itemJpa)
        return item
    }
}
