package help.me.utils

import help.me.authentication.service.AccountService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
class SpringConfiguration
    (
    private val accountService: AccountService,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) {

    private val authProvider = DaoAuthenticationProvider()
        .also {
            it.setPasswordEncoder(bCryptPasswordEncoder)
            it.setUserDetailsService(accountService)
        }

    @Bean
    fun authProvider(): AuthenticationProvider {
        return authProvider
    }

    @Bean
    fun authManager(http: HttpSecurity): AuthenticationManager {
        return http.getSharedObject(AuthenticationManagerBuilder::class.java)
            .authenticationProvider(authProvider)
            .build()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .authenticationProvider(authProvider)
            .authorizeHttpRequests {
                it.requestMatchers("/api/**").authenticated()
            }
            .build()
    }
}