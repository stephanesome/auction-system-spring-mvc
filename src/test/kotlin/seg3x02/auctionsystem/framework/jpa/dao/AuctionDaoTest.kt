package seg3x02.auctionsystem.framework.jpa.dao

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.repository.findByIdOrNull
import seg3x02.auctionsystem.adapters.repositories.AuctionJpaAdapter
import seg3x02.auctionsystem.domain.auction.entities.Auction
import seg3x02.auctionsystem.domain.auction.entities.AuctionCategory
import seg3x02.auctionsystem.framework.jpa.entities.auction.AuctionCategoryJpaEntity
import seg3x02.auctionsystem.framework.jpa.entities.auction.AuctionJpaEntity
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

@DataJpaTest
class AuctionDaoTest {
    @Autowired
    lateinit var auctionJpaAdapter: AuctionJpaAdapter

    @Autowired
    lateinit var entityManager: TestEntityManager

    @Autowired
    lateinit var auctionRepository: AuctionJpaRepository

    @TestConfiguration
    internal class TestConfig {
        @Bean
        fun auctionJpaAdapter(auctionRepository: AuctionJpaRepository): AuctionJpaAdapter {
            return AuctionJpaAdapter(auctionRepository)
        }
    }

    @Test
    fun save_auction() {
        val sellerId = "seller000"
        val auctionId = UUID.randomUUID()
        val auction = Auction(auctionId,
            LocalDateTime.now(),
            Duration.ofDays(3),
            BigDecimal(100),
            BigDecimal(5),
            sellerId,
            AuctionCategory("Toy"),
            false
        )
        val itemId = UUID.randomUUID()
        auction.item = itemId
        auction.bids.add(UUID.randomUUID())
        auction.bids.add(UUID.randomUUID())

        auctionJpaAdapter.save(auction)

        val auctionFound = auctionRepository.findByIdOrNull(auctionId)
        Assertions.assertThat(auctionFound).isNotNull
        Assertions.assertThat(auctionFound?.bids).isNotEmpty
        Assertions.assertThat(auctionFound?.item).isEqualTo(auction.item)
    }

    @Test
    fun find_auction() {
        val sellerId = "seller001"
        val auctionId = UUID.randomUUID()
        val auction = AuctionJpaEntity(auctionId,
            LocalDateTime.now(),
            Duration.ofDays(3),
            BigDecimal(100),
            BigDecimal(5),
            UUID.randomUUID(),
            sellerId,
            AuctionCategoryJpaEntity("Toy"),
            false
        )
        auction.bids.add(UUID.randomUUID())
        auction.bids.add(UUID.randomUUID())

        entityManager.persist(auction)
        entityManager.flush()
    }
}
