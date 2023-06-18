package modules.users.repositories

import modules.users.domain.UserEntity

interface ICsvUserRepo {
    fun getAll(): List<UserEntity>
}



