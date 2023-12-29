package help.me.utils.exceptions

import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity

open class BasicThrowable(val status: HttpStatusCode, message: String) : Throwable(message) {
    open fun toBody(): Any {
        return mapOf("message" to message)
    }

    fun toResponseEntity(): ResponseEntity<*> {
        return ResponseEntity.status(status).body(toBody())
    }
}

fun Throwable.toResponseEntity(): ResponseEntity<*> {
    if (this is BasicThrowable) return this.toResponseEntity()

    return ResponseEntity.internalServerError().body(mapOf("message" to this.localizedMessage))
}

fun Result<*>.foldToResponseEntity(): ResponseEntity<*> {
    return this.fold(
        { ResponseEntity.ok(it) },
        { it.toResponseEntity() }
    )
}