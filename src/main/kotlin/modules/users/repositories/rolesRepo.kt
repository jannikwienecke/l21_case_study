package modules.users.repositories

import modules.users.domain.UserRole

interface ICsvRolesRepo {
    fun getAll(): List<UserRole>
}



