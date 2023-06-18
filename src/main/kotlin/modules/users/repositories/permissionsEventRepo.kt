package modules.users.repositories

import modules.users.domain.PermissionEvent


interface PermissionsEventRepo {
    fun saveEvent(userId: Int, permissionEvents: List<PermissionEvent?>): Boolean
}
