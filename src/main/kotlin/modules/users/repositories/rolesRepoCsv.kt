package modules.users.repositories

import modules.users.domain.UserRole
import modules.users.mappers.UserRoleMap
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader


class CsvRolesRepo(private val fileName: String) : ICsvRolesRepo {
    override fun getAll(): List<UserRole> {
        val userRoles = mutableListOf<UserRole>()

        val file = File(fileName)
        if (!file.exists()) {
            throw FileNotFoundException("File Does Not Exists $fileName")
        }

        val reader = BufferedReader(FileReader(fileName))
        reader.use { r ->
            var lines = r.readLines()

            for (i in 1 until lines.size) {
                var line = lines[i]
                val row = line.split(",")

                if (row.size >= 2) {
                    val userRole = UserRoleMap().toDomain(
                        mapOf(
                            "userId" to row[0],
                            "role" to row[1]
                        )
                    )
                    userRoles.add(userRole)
                } else {
                    throw Exception("Invalid Row in CsvRoles File")
                }
            }
        }

        return userRoles
    }
}