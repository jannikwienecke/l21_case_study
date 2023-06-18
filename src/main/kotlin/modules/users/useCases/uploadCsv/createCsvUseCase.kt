package modules.users.useCases.uploadCsv

import modules.users.domain.PermissionEvent
import modules.users.domain.UserEntity
import modules.users.domain.UserRole
import modules.users.repositories.CsvRolesRepo
import modules.users.repositories.CsvUserRepo
import modules.users.repositories.PermissionsEventRepo
import modules.users.repositories.UserProfileRepo

class createCsvUseCase(
    userCsvRepo: CsvUserRepo,
    userRoleCsvRepo: CsvRolesRepo,
    permissionsEventRepo: PermissionsEventRepo,
    userProfileRepo: UserProfileRepo
) {
    val _userCsvRepo = userCsvRepo
    val _userRoleCsvRepo = userRoleCsvRepo
    val _userProfileRepo = userProfileRepo
    val _permissionEventRepo = permissionsEventRepo

    fun execute() {
        println("Execute Use Case Create CSV")

        val importedUsers = this._userCsvRepo.getAll()
        val importedUserRoles = this._userRoleCsvRepo.getAll()

        println("Update User in DB...")
        upsertUsers(importedUsers)
        println("Update Users Done")

        println("Update User Permissions in DB")
        upsertUsersPermissions(importedUserRoles)
        println("Update User Permissions Done")


    }

    private fun upsertUsers(users: List<UserEntity>) {
        for (user in users) {
            this._userProfileRepo.save(userEntity = user)
        }
    }

    private fun upsertUsersPermissions(userRoles: List<UserRole>) {
        val uniqueUserIds = userRoles.map { it.id }.distinct()

        uniqueUserIds.forEach {
            val userId = it
            val permissionEvents = userRoles.filter { it.id == userId }.map { PermissionEvent.fromString(it.role) }
            if (permissionEvents.isEmpty()) return

            this._permissionEventRepo.saveEvent(userId = userId, permissionEvents = permissionEvents)
        }
    }
}