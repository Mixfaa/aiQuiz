package help.me

import help.me.utils.exceptions.BasicThrowable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody


@ControllerAdvice
class ControllerAdvice {
    @ResponseBody
    @ExceptionHandler(BasicThrowable::class)
    fun handleAnyThrowable(ex: BasicThrowable): ResponseEntity<*> {
        return ex.toResponseEntity()
    }
}