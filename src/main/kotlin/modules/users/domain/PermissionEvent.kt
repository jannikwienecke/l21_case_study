package modules.users.domain

enum class PermissionEvent(val id: Int) {
    ADD_READ(1),
    ADD_WRITE(2),
    ADD_DELETE(3),
    DELETE_READ(4),
    DELETE_WRITE(5),
    DELETE_DELETE(6);

    companion object {
        fun fromString(value: String): PermissionEvent? {
            return when (value.lowercase()) {
                "read" -> ADD_READ
                "write" -> ADD_WRITE
                "delete" -> ADD_DELETE
                else -> throw Exception("INVALID ROLE NAME")
            }
        }

        fun getReversedEvent(id: Int): PermissionEvent {
            return when (id) {
                1 -> PermissionEvent.DELETE_READ
                2 -> PermissionEvent.DELETE_WRITE
                3 -> PermissionEvent.DELETE_DELETE
                else -> throw Exception("INVALID PERMISSION EVENT")
            }
        }

        fun canWrite(ids: List<Int>): Boolean {
            return this.hasPermission(ids, 2, 5)
        }

        fun canRead(ids: List<Int>): Boolean {
            return this.hasPermission(ids, 1, 4)
        }

        fun canDelete(ids: List<Int>): Boolean {
            return this.hasPermission(ids, 3, 6)
        }

        private fun hasPermission(ids: List<Int>, addId: Int, removeId: Int): Boolean {

            val add = ids.indexOf(addId)
            val remove = ids.indexOf(removeId)

            if (add == -1) return false
            if (add != -1 && remove == -1) return true
            if (add < remove) return true

            return false
        }
    }
}

