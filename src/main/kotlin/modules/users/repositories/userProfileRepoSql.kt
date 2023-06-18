package modules.users.repositories

import modules.users.domain.UserEntity
import java.sql.Connection

class UserProfileRepoSql(sqlConnection: Connection) : UserProfileRepo {
    val _sqlConn = sqlConnection

    override fun save(userEntity: UserEntity): Boolean {
        val selectStatement = this._sqlConn.prepareStatement("SELECT email FROM users WHERE id = ?")
        val updateStatement = this._sqlConn.prepareStatement("UPDATE users SET email = ? WHERE id = ?")
        val insertStatement = this._sqlConn.prepareStatement("INSERT INTO users (id, email) VALUES (?, ?)")

        selectStatement.setInt(1, userEntity.id)
        val resultSet = selectStatement.executeQuery()

        if (resultSet.next()) {
            val existingEmail = resultSet.getString("email")

            // If user is in the database and email has changed, update the user
            if (existingEmail != userEntity.email.value) {
                updateStatement.setString(1, userEntity.email.value)
                updateStatement.setInt(2, userEntity.id)
                updateStatement.executeUpdate()
            }

        } else {
            // If user is not in the database, insert the user
            insertStatement.setInt(1, userEntity.id)
            insertStatement.setString(2, userEntity.email.value)
            insertStatement.executeUpdate()
        }

        return true
    }
}
