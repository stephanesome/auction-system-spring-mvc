package seg3x02.auctionsystem.infrastructure.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import seg3x02.auctionsystem.infrastructure.security.UserDetailsServiceImpl


@Configuration
@EnableWebSecurity
class SecurityConfiguration(private val userDetailsService: UserDetailsServiceImpl) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf(({ csrf -> csrf.disable() }))
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/**").permitAll()
                    .requestMatchers("/auth/**").hasRole("USER")
                }
                .formLogin { form -> form
                    .loginPage("/login")
                    .defaultSuccessUrl("/auth/account")
                    .permitAll()}
                .logout { logout -> logout
                    .logoutSuccessUrl("/")
                    .permitAll()}
        return http.build()
    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web: WebSecurity ->
            web.ignoring()
                .requestMatchers("/resources/**",  "/css/**", "/js/**", "/images/**","/vendor/**","/fonts/**") }
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder? {
        return BCryptPasswordEncoder()
    }
}
