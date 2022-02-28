package seg3x02.auctionsystem.adapters.repositories.converters

import org.mapstruct.Mapper
import seg3x02.auctionsystem.domain.auction.core.Bid
import seg3x02.auctionsystem.domain.user.core.creditCard.Address
import seg3x02.auctionsystem.domain.user.core.creditCard.CreditCard
import seg3x02.auctionsystem.framework.jpa.entities.auction.BidJpaEntity
import seg3x02.auctionsystem.framework.jpa.entities.user.creditCard.AddressJpaEntity
import seg3x02.auctionsystem.framework.jpa.entities.user.creditCard.CreditCardJpaEntity

@Mapper
interface CreditCardJpaConverter {
    fun convertToJpa(creditCard: CreditCard): CreditCardJpaEntity
    fun convertAddressToJpa(address: Address): AddressJpaEntity
    fun convertToModel(creditCardJpa: CreditCardJpaEntity): CreditCard
    fun convertAddressToModel(address: AddressJpaEntity): Address
}
