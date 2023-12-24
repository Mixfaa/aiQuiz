package help.me.utils.exceptions

import org.springframework.http.HttpStatus

class GenerationFailed(message: String) : BasicThrowable(HttpStatus.METHOD_FAILURE, message) {
    constructor(throwable: Throwable) : this(throwable.localizedMessage)
}