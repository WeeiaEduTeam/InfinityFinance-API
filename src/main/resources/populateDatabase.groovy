import groovy.transform.Field
import groovy.time.*
import java.time.*

@Field int counter = 1

def createFile(name) {
    def currentDirPath = System.getProperty("user.dir")
    def pathToInsertData = "/db/data/02-insert/"

    def fullName = currentDirPath + pathToInsertData + counter++ + "-insert-" + name

    return new File(fullName)
}

def clearFileAndGenerateHeader(file) {
    clearFile(file)
    generateHeader(file)
}

def clearFile(file) {
    file.write("")
}

def generateHeader(file) {
    def sqlHeader = "--liquibase formatted sql"
    def currentChangeset = counter - 1
    def changesetHeader = "--changeset pacion:" + currentChangeset

    file.append(sqlHeader + "\n")
    file.append(changesetHeader + "\n")
}

def generateCategories() {
    File categories = createFile("categories.sql")
    clearFileAndGenerateHeader(categories)

    for (int i = 1; i <= 200; i++) {
        def exampleTimestamp = LocalDateTime.now().minusDays(100 - i)

        def categoryInsert = "INSERT INTO category (name, created, updated) " +
            "VALUES ('Test name ${i}','" + exampleTimestamp + "','" + exampleTimestamp + "');\n"

        categories.append(categoryInsert)
    }
}

def generateRoles() {
    File roles = createFile("roles.sql")
    clearFileAndGenerateHeader(roles)

    roles.append("INSERT INTO role (name) " +
            "VALUES ('ROLE_USER');\n")

    roles.append("INSERT INTO role (name) " +
            "VALUES ('ROLE_ADMIN');\n")
}

def generateUsers() {
    File users = createFile("users.sql")
    clearFileAndGenerateHeader(users)

    def exampleTimestamp = LocalDateTime.now()

    users.append("INSERT INTO app_user (username, email, password, first_name, second_name, created, updated)" +
            "VALUES ('admin', 'admin@wp.pl', '{noop}admin', 'admin', 'admin','" + exampleTimestamp + "','" + exampleTimestamp + "');\n")

    users.append("INSERT INTO app_user (username, email, password, first_name, second_name, created, updated)" +
            "VALUES ('user', 'user@wp.pl', '{noop}user', 'user', 'user','" + exampleTimestamp + "','" + exampleTimestamp + "');\n")

    for (int i = 1; i <= 20; i++) {
        exampleTimestamp = LocalDateTime.now().minusDays(100 - i)

        def categoryInsert = "INSERT INTO app_user (username, email, password, first_name, second_name, created, updated)" +
                "VALUES ('test${i}', 'test${i}@wp.pl', '{noop}test${i}', 'test${i}', 'test${i}','" + exampleTimestamp + "','" + exampleTimestamp + "');\n"

        users.append(categoryInsert)
    }
}

def generateRelationUserRole() {
    File relation = createFile("relationUserRole.sql")
    clearFileAndGenerateHeader(relation)

    def userRole = 1
    def adminRole = 2
    def adminId = 1
    def userId = 2

    relation.append("INSERT INTO user_roles (role_id, user_id) " +
            "VALUES(" + userRole + "," + adminId + ");\n")

    relation.append("INSERT INTO user_roles (role_id, user_id) " +
            "VALUES(" + adminRole + "," + adminId + ");\n")

    relation.append("INSERT INTO user_roles (role_id, user_id) " +
            "VALUES(" + userRole + "," + userId + ");\n")

    def numberOfUsersInserted = 22

    for (int i = 3; i <= numberOfUsersInserted; i++) {

        def randomRole = (i % 2) + 1

        def categoryInsert = "INSERT INTO user_roles (role_id, user_id) " +
                "VALUES(" + randomRole + "," + i + ");\n"

        relation.append(categoryInsert)

        // admin has to obtain user role
        if(randomRole == adminRole) {
            categoryInsert = "INSERT INTO user_roles (role_id, user_id) " +
                    "VALUES(" + userRole + "," + i + ");\n"

            relation.append(categoryInsert)
        }
    }
}

def generateTransactions() {
    File categories = createFile("transactions.sql")
    clearFileAndGenerateHeader(categories)

    def transactionsMap = [0:"INCOME", 1:"OUTCOME"]

    def numberOfUsers = 22
    for (int i = 1; i <= 40000; i++) {
        def exampleTimestamp = LocalDateTime.now().minusDays(i)

        def randomId = Math.abs(new Random().nextInt() % 2)
        def value = i + i + (i % 10) * 10 + i % 2
        def quantity = (i % 9) + 1
        def category = (i % 200) + 1
        def user = (i % numberOfUsers) + 1

        def categoryInsert = "INSERT INTO transaction (transaction_type, value, quantity, title, description, category_id, appuser_id, created, updated) VALUES " +
                "('" + transactionsMap[randomId] + "', ${value}, ${quantity}, 'Title ${i}', 'Description ${i}', ${category}, '${user}' ,'" + exampleTimestamp + "','" + exampleTimestamp + "');\n"

        categories.append(categoryInsert)
    }

}

generateRoles()
generateUsers()
generateRelationUserRole()
generateCategories()
generateTransactions()