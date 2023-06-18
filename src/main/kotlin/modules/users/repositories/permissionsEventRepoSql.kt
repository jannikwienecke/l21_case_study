package modules.users.repositories

import modules.users.domain.PermissionEvent
import java.sql.Connection

data class Record(val id: Int, val permissionEventId: Int)


class PermissionEventRepoSql(sqlConnection: Connection) : PermissionsEventRepo {
    val _sqlConn = sqlConnection


    //    TODO: MOVE BUSINESS LOGIC IN Domain Object PermissionEvent
//    AND ONLY CALL FUNCTION THAT UPDATE THE DB
    override fun saveEvent(userId: Int, permissionEvents: List<PermissionEvent?>): Boolean {

        val records = this.getRecordsOfEvent(userId = userId)

        val permissionEventIds = records.map { it.permissionEventId }

        val canWrite = PermissionEvent.canWrite(permissionEventIds)
        val canRead = PermissionEvent.canRead(permissionEventIds)
        val canDelete = PermissionEvent.canDelete(permissionEventIds)

        handleEvent(
            permissionEvents = permissionEvents,
            event = PermissionEvent.ADD_READ,
            hasCurrentlyPermission = canRead,
            userId = userId
        )

        handleEvent(
            permissionEvents = permissionEvents,
            event = PermissionEvent.ADD_DELETE,
            hasCurrentlyPermission = canDelete,
            userId = userId
        )

        handleEvent(
            permissionEvents = permissionEvents,
            event = PermissionEvent.ADD_WRITE,
            hasCurrentlyPermission = canWrite,
            userId = userId
        )

        return true
    }


    private fun handleEvent(
        permissionEvents: List<PermissionEvent?>,
        event: PermissionEvent,
        hasCurrentlyPermission: Boolean,
        userId: Int
    ) {

        val hasStillPermission = permissionEvents.find { it?.id == event.id } != null

        if (!hasStillPermission && hasCurrentlyPermission) {
            this._sqlConn.prepareStatement(
                "INSERT INTO PermissionEvents (userId, permissionEventId) VALUES(?, ?)"
            ).also {
                it.setInt(1, userId)
                it.setInt(2, PermissionEvent.getReversedEvent(event.id).id)
                it.executeUpdate()
            }

        } else if (hasStillPermission && !hasCurrentlyPermission) {

            this._sqlConn.prepareStatement(
                "INSERT INTO PermissionEvents (userId, permissionEventId) VALUES(?, ?)"
            ).also {
                it.setInt(1, userId)
                it.setInt(2, event.id)
                it.executeUpdate()
            }
        }
    }

    private fun getRecordsOfEvent(userId: Int): ArrayList<Record> {
        val selectStatement =
            _sqlConn.prepareStatement("SELECT DISTINCT ON(permissionEventId) * FROM PermissionEvents WHERE userId = ? ORDER BY permissionEventId, id DESC LIMIT 6")

        selectStatement.setInt(1, userId)

        val resultSet = selectStatement.executeQuery()

        val records = ArrayList<Record>()
        while (resultSet.next()) {
            records.add(Record(resultSet.getInt("id"), resultSet.getInt("permissionEventId")))
        }
        records.sortByDescending { it.id }

        return records

    }

}

