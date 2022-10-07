package seg3x02.auctionsystem.adapters.services.implementation.application

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import seg3x02.auctionsystem.application.services.DomainEventEmitter
import seg3x02.auctionsystem.domain.common.DomainEvent

@Component
class DomainEventEmitterAdapter: DomainEventEmitter {
    @Autowired
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    override fun emit(event: DomainEvent) {
        applicationEventPublisher.publishEvent(event)
    }
}
