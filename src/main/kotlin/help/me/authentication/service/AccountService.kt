package help.me.authentication.service

import help.me.authentication.model.Account
import help.me.authentication.model.RegisterRequest
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AccountService(private val accountRepository: AccountRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return accountRepository.findByUsername(username).block()
            ?: throw UsernameNotFoundException("User not found")
    }

    fun registerAccount(request: RegisterRequest): Result<Account> {
        if (accountRepository.existsByUsername(request.username).block() == true)
            return Result.failure(Throwable("User with this username already exists"))

        val account = accountRepository.save(
            Account(
                request.username,
                request.password
            )
        ).block()
            ?: return Result.failure(Throwable("Database returned null"))

        return Result.success(account)
    }
}