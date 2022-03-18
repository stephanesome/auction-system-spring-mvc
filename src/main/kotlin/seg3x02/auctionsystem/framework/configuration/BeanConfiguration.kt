package seg3x02.auctionsystem.framework.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import seg3x02.auctionsystem.application.services.AuctionScheduler
import seg3x02.auctionsystem.application.services.CreditService
import seg3x02.auctionsystem.application.services.EmailService
import seg3x02.auctionsystem.application.services.DomainEventEmitter
import seg3x02.auctionsystem.application.usecases.*
import seg3x02.auctionsystem.application.usecases.implementation.*
import seg3x02.auctionsystem.domain.auction.facade.AuctionFacade
import seg3x02.auctionsystem.domain.auction.facade.implementation.AuctionFacadeImpl
import seg3x02.auctionsystem.domain.auction.factories.AuctionFactory
import seg3x02.auctionsystem.domain.auction.repositories.AuctionRepository
import seg3x02.auctionsystem.domain.item.facade.ItemFacade
import seg3x02.auctionsystem.domain.item.facade.implementation.ItemFacadeImpl
import seg3x02.auctionsystem.domain.item.factories.ItemFactory
import seg3x02.auctionsystem.domain.item.repositories.ItemRepository
import seg3x02.auctionsystem.domain.user.facade.UserFacade
import seg3x02.auctionsystem.domain.user.facade.implementation.UserFacadeImpl
import seg3x02.auctionsystem.domain.user.factories.AccountFactory
import seg3x02.auctionsystem.domain.user.factories.CreditCardFactory
import seg3x02.auctionsystem.domain.user.repositories.CreditCardRepository
import seg3x02.auctionsystem.domain.user.repositories.AccountRepository

@Configuration
// @ComponentScan(basePackageClasses = [AuctionSystemApplication::class])
class BeanConfiguration() {
    @Bean
    fun createAuctionUseCase(userFacade: UserFacade,
                             itemFacade: ItemFacade,
                             auctionFacade: AuctionFacade,
                             auctionScheduler: AuctionScheduler): CreateAuction {
        return CreateAuctionImpl(userFacade, itemFacade, auctionFacade, auctionScheduler)
    }

    @Bean
    fun placeBidUseCase(userFacade: UserFacade,
                        auctionFacade: AuctionFacade): PlaceBid {
        return PlaceBidImpl(userFacade, auctionFacade)
    }

    @Bean
    fun closeAuctionUseCase(userFacade: UserFacade,
                            auctionFacade: AuctionFacade,
                            creditService: CreditService,
                            emailService: EmailService
    ): CloseAuction {
        return CloseAuctionImpl(userFacade, auctionFacade, creditService, emailService)
    }

    @Bean
    fun createAccountUseCase(userFacade: UserFacade): CreateAccount {
        return CreateAccountImpl(userFacade)
    }

    @Bean
    fun updateAccountUseCase(userFacade: UserFacade): UpdateAccount {
        return UpdateAccountImpl(userFacade)
    }

    @Bean
    fun deactivateAccountUseCase(userFacade: UserFacade,
                                 auctionFacade: AuctionFacade,
                        emailService: EmailService
    ): DeactivateAccount {
        return DeactivateAccountImpl(userFacade, auctionFacade, emailService)
    }

    @Bean
    fun browseAuctionsUseCase(auctionFacade: AuctionFacade,
                              itemFacade: ItemFacade
    ): BrowseAuctions {
        return BrowseAuctionsImpl(auctionFacade, itemFacade)
    }

    @Bean
    fun viewAccountUseCase(userFacade: UserFacade
    ): ViewAccount {
        return ViewAccountImpl(userFacade)
    }

    @Bean
    fun userFacade(accountRepository: AccountRepository,
                   accountFactory: AccountFactory,
                   creditCardRepository: CreditCardRepository,
                   creditCardFactory: CreditCardFactory,
                   eventEmitter: DomainEventEmitter,
                   creditService: CreditService
    ): UserFacade {
        return UserFacadeImpl(accountRepository,
            accountFactory,
            creditCardRepository,
            creditCardFactory,
            eventEmitter,
            creditService)
    }

    @Bean
    fun itemFacade(itemFactory: ItemFactory,
            itemRepository: ItemRepository,
            eventEmitter: DomainEventEmitter
    ): ItemFacade {
        return ItemFacadeImpl(itemFactory, itemRepository, eventEmitter)
    }

    @Bean
    fun auctionFacade(auctionFactory: AuctionFactory,
            auctionRepository: AuctionRepository,
            eventEmitter: DomainEventEmitter
    ): AuctionFacade {
        return AuctionFacadeImpl(auctionFactory, auctionRepository, eventEmitter)
    }
}
