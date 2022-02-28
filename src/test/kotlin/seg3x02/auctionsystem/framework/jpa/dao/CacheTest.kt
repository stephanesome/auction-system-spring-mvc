package seg3x02.auctionsystem.framework.jpa.dao

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import seg3x02.auctionsystem.adapters.repositories.ItemJpaAdapter
import seg3x02.auctionsystem.domain.item.core.Item
import java.util.*

@DataJpaTest
class CacheTest {
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
    fun test_cache() {
        val itemId1 = UUID.randomUUID()
        val item1 = Item(itemId1,
            "Game boy",
            "Still wrapped in")
        itemJpaAdapter.save(item1)

        val item2 = itemJpaAdapter.find(itemId1)
        val item3 = itemJpaAdapter.find(itemId1)
        itemJpaAdapter.find(itemId1)

        val itemId2 = UUID.randomUUID()
        val item4 = Item(itemId2,
            "Game boy II",
            "Still wrapped in Good")

        itemJpaAdapter.save(item4)
        val item5 = itemJpaAdapter.find(itemId2)
        val item6 = itemJpaAdapter.find(itemId1)
        itemJpaAdapter.find(itemId1)
        itemJpaAdapter.find(itemId2)

        itemJpaAdapter.save(item1)

        val itemId3 = UUID.randomUUID()
        val item7 = Item(itemId3,
            "Game boy XXX",
            "Still wrapped in")
        itemJpaAdapter.save(item7)
    }
}
