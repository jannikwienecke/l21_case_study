package modules.users.repositories

import modules.users.domain.UserEntity


interface UserProfileRepo {
    fun save(userEntity: UserEntity): Boolean
}