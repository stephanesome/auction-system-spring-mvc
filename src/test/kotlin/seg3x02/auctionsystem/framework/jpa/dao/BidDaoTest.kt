package seg3x02.auctionsystem.framework.jpa.dao

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.repository.findByIdOrNull
import seg3x02.auctionsystem.adapters.repositories.BidJpaAdapter
import seg3x02.auctionsystem.domain.auction.core.Bid
import seg3x02.auctionsystem.framework.jpa.entities.auction.BidJpaEntity
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@DataJpaTest
class BidDaoTest {
    @Autowired
    lateinit var bidJpaAdapter: BidJpaAdapter

    @Autowired
    lateinit var entityManager: TestEntityManager

    @Autowired
    lateinit var bidRepository: BidJpaRepository

    @TestConfiguration
    internal class TestConfig {
        @Bean
        fun bidJpaAdapter(bidRepository: BidJpaRepository): BidJpaAdapter {
            return BidJpaAdapter(bidRepository)
        }
    }

    @Test
    fun save_bid() {
        val bidId = UUID.randomUUID()
        val buyerId = UUID.randomUUID()
        val bid = Bid(bidId,
            BigDecimal(200.45),
            LocalDateTime.now(),
            buyerId)

        bidJpaAdapter.save(bid)

        val bidFound = bidRepository.findByIdOrNull(bidId)

        Assertions.assertThat(bidFound).isNotNull
        Assertions.assertThat(bidFound?.amount).isEqualTo(bid.amount)
    }

    @Test
    fun find_bid() {
        val bidId = UUID.randomUUID()
        val buyerId = UUID.randomUUID()
        val bid = BidJpaEntity(bidId,
            BigDecimal(200.45),
            LocalDateTime.now(),
            buyerId)

        entityManager.persist(bid)
        entityManager.flush()

        val bidFound = bidJpaAdapter.find(bidId)

        Assertions.assertThat(bidFound).isNotNull
        Assertions.assertThat(bidFound?.amount).isEqualTo(bid.amount)
    }
}
