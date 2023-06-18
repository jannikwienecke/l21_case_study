package modules.users.domain

data class UserRole(val id: Int, val role: String) {
    init {
        if (id == 0) {
            throw IllegalArgumentException("User id should be greater than 0")
        }
    }
}

