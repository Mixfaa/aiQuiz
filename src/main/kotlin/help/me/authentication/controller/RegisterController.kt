package help.me.authentication.controller

import help.me.authentication.model.RegisterRequest
import help.me.authentication.service.AccountService
import help.me.utils.exceptions.foldToResponseEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RegisterController(
    private val accountService: AccountService
) {
    @PostMapping("/register")
    fun register(request: RegisterRequest): ResponseEntity<*> {
        return accountService.registerAccount(request).foldToResponseEntity()
    }
}