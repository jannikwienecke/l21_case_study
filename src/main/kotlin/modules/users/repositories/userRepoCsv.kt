package modules.users.repositories

import modules.users.domain.UserEntity
import modules.users.mappers.UserMap
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.security.InvalidParameterException


class CsvUserRepo(private val fileName: String) : ICsvUserRepo {
    override fun getAll(): List<UserEntity> {
        val users = mutableListOf<UserEntity>()

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
                    val user = UserMap().toDomain(
                        mapOf(
                            "id" to row[0],
                            "email" to row[1]
                        )
                    )
                    users.add(user)
                } else {
                    throw InvalidParameterException("Invalid Row $row")
                }
            }
        }

        return users
    }
}