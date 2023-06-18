import modules.users.domain.Email
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class `EmailTest` {

    @Test
    fun `Email value is valid`() {
        val validEmail = "test@example.com"
        val email = Email(validEmail)
        Assertions.assertEquals(validEmail, email.value)
    }

    @Test
    fun `Email value is invalid`() {
        val invalidEmail = "invalidEmail"
        Assertions.assertThrows(IllegalArgumentException::class.java) { Email(invalidEmail) }
    }
}
