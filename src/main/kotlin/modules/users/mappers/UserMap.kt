package modules.users.mappers

import modules.users.domain.Email
import modules.users.domain.UserEntity


class UserMap {
    fun toDomain(userMap: Map<String, Any>): UserEntity {
        val id = (userMap["id"] as String).toInt()
        val emailValue = userMap["email"] as String

        val email = Email(emailValue)
        val user = UserEntity(id, email)

        return user
    }
}