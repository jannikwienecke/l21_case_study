package modules.users.domain

data class UserEntity(val id: Int, val email: Email) {
    init {
        if (id == 0) {
            throw IllegalArgumentException("User id should be greater than 0")
        }
    }
}

