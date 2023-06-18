package modules.users.mappers

import modules.users.domain.UserRole


class UserRoleMap {
    fun toDomain(userMap: Map<String, Any>): UserRole {
        val userId = (userMap["userId"] as String).toInt()
        val role = userMap["role"] as String

        return UserRole(userId, role)

    }
}