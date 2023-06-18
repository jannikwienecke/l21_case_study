package modules.users.domain

data class Email(val value: String) {
    init {
        if (!isEmailValid(value)) {
            throw IllegalArgumentException("Invalid email address")
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()
        return emailRegex.matches(email)
    }

}



