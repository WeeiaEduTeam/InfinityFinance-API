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

    for (int i = 1; i <= 20; i++) {
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
            "VALUES ('admin', 'admin@wp.pl', 'admin', '{noop}admin', 'admin','" + exampleTimestamp + "','" + exampleTimestamp + "');\n")

    users.append("INSERT INTO app_user (username, email, password, first_name, second_name, created, updated)" +
            "VALUES ('user', 'user@wp.pl', 'user', '{noop}user', 'user','" + exampleTimestamp + "','" + exampleTimestamp + "');\n")

    for (int i = 1; i <= 20; i++) {
        exampleTimestamp = LocalDateTime.now().minusDays(100 - i)

        def categoryInsert = "INSERT INTO app_user (username, email, password, first_name, second_name, created, updated)" +
                "VALUES ('test${i}', 'test${i}@wp.pl', 'test${i}', '{noop}test${i}', 'test${i}','" + exampleTimestamp + "','" + exampleTimestamp + "');\n"

        users.append(categoryInsert)
    }
}

generateCategories()
generateRoles()
generateUsers()