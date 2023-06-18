import modules.users.CSV_ROLES_PATH
import modules.users.CSV_USERS_PATH
import modules.users.repositories.CsvRolesRepo
import modules.users.repositories.CsvUserRepo
import modules.users.repositories.PermissionEventRepoSql
import modules.users.repositories.UserProfileRepoSql
import modules.users.useCases.uploadCsvFile.uploadCsvFile
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

fun main(args: Array<String>) {

    println("⏳Program arguments: ${args.joinToString()}")

    if (checkIfFileChanged()) {
        println("FILE DID NOT CHANGE SINCE LAST")
        return
    }

    val sqlConnection = getSqlConnection()

//    We want to have one connection to improve perfomance and update all or nothing
//    so that we are always in sync with the orignal database
    sqlConnection.autoCommit = false


    val csvUserRepo = CsvUserRepo(fileName = CSV_USERS_PATH)
    val csvRolesRepo = CsvRolesRepo(fileName = CSV_ROLES_PATH)
    val permissionEventRepo = PermissionEventRepoSql(sqlConnection = sqlConnection)
    val userProfileRepo = UserProfileRepoSql(sqlConnection = sqlConnection)

    uploadCsvFile(
        userCsvRepo = csvUserRepo,
        userRoleCsvRepo = csvRolesRepo,
        permissionsEventRepo = permissionEventRepo,
        userProfileRepo = userProfileRepo
    ).execute()

    try {

//    COMMIT ALL CHANGES AT ONE
        println("Commit all changes")
        sqlConnection.commit()
    } catch (exception: SQLException) {
        println("🚨Something went wrong: Rollback...")
        sqlConnection.rollback()
        println("Exception: $exception")
    }

    println("✅ Successfully uploaded CSV File! ")


}

fun getSqlConnection(): Connection {
//    TODO: READ FROM AN .env FILE
    val url = "jdbc:postgresql://localhost/postgres"
    val user = "postgres"
    val password = "postgres"

    var conn: Connection? = null
    conn = DriverManager.getConnection(url, user, password)

    return conn
}

// TODO: NEED TO REMEMBER THE DATE OF THE TIME WE LAST UPLOADED
// IF SAME DATE -> DONT RUN PROGRAMM
fun checkIfFileChanged(): Boolean {
    val file = File(CSV_USERS_PATH);
    if (file.exists()) {
        val lastModified = file.lastModified()
    }

//    FOR NOW: ALWAYS RETURN FALSE
    return false
}


//SQL STATEMENTS
//CREATE TABLE Users (id int, email varchar(255));
//CREATE TABLE Permissions (id int, permissions varchar(255));
//CREATE TABLE PermissionEvents (id int, userId int, permissionEventId int);

// INSERT INTO PERMISSIONS VALUES(1, 'ADD_READ');
// INSERT INTO PERMISSIONS VALUES(2, 'ADD_DELETE');
// INSERT INTO PERMISSIONS VALUES(3, 'ADD_WRITE');
// INSERT INTO PERMISSIONS VALUES(4, 'REMOVE_READ');
// INSERT INTO PERMISSIONS VALUES(5, 'REMOVE_DELETE');
// INSERT INTO PERMISSIONS VALUES(6, 'REMOVE_WRITE');