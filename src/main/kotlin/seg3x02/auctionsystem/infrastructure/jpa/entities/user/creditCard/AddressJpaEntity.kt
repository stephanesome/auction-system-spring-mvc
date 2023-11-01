package seg3x02.auctionsystem.infrastructure.jpa.entities.user.creditCard

import jakarta.persistence.Embeddable

@Embeddable
class AddressJpaEntity(var street: String,
                       var city: String,
                       var country: String,
                       var postalCode: String) {
}
