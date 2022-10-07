package seg3x02.auctionsystem.infrastructure.configuration

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.stereotype.Component

@Component
class CacheCustomizer: CacheManagerCustomizer<ConcurrentMapCacheManager> {
    override fun customize(cacheManager: ConcurrentMapCacheManager?) {
        cacheManager?.setCacheNames(listOf("users", "auctions", "items", "bids", "creditCards"))
    }
}
