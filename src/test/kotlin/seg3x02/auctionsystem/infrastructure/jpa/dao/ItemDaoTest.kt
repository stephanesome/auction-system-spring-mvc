package seg3x02.auctionsystem.infrastructure.jpa.dao

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.repository.findByIdOrNull
import seg3x02.auctionsystem.adapters.repositories.ItemJpaAdapter
import seg3x02.auctionsystem.domain.item.entities.Item
import seg3x02.auctionsystem.infrastructure.jpa.entities.item.ItemJpaEntity
import java.util.*

@DataJpaTest
class ItemDaoTest {
    @Autowired
    lateinit var itemJpaAdapter: ItemJpaAdapter

    @Autowired
    lateinit var entityManager: TestEntityManager

    @Autowired
    lateinit var itemRepository: ItemJpaRepository

    @TestConfiguration
    internal class TestConfig {
        @Bean
        fun itemJpaAdapter(itemRepository: ItemJpaRepository): ItemJpaAdapter {
            return ItemJpaAdapter(itemRepository)
        }
    }

    @Test
    fun save_item() {
        val itemId = UUID.randomUUID()
        val item = Item(itemId,
            "Game boy",
            "Still wrapped in")

        itemJpaAdapter.save(item)

        val itemFound = itemRepository.findByIdOrNull(itemId)

        Assertions.assertThat(itemFound).isNotNull
        Assertions.assertThat(itemFound?.description).isEqualTo(item.description)
    }

    @Test
    fun find_item() {
        val itemId = UUID.randomUUID()
        val item = ItemJpaEntity(itemId,
            "Game boy",
            "Still wrapped in")

        entityManager.persist(item)
        entityManager.flush()

        val itemFound = itemJpaAdapter.find(itemId)

        Assertions.assertThat(itemFound).isNotNull
        Assertions.assertThat(itemFound?.description).isEqualTo(item.description)
    }
}
