import groovy.transform.Field
import groovy.time.*
import java.time.*

@Field int counter = 2

def createFile(name) {
    def currentDirPath = System.getProperty("user.dir")
    def pathToInsertData = "/db/data/02-insert/"

    def fullName = currentDirPath + pathToInsertData + counter++ + "-" + name

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
    File dataSql = createFile("category.sql")
    clearFileAndGenerateHeader(dataSql)

    for (int i = 1; i <= 20; i++) {
        def exampleTimestamp = LocalDateTime.now().minusDays(100 - i)

        def categoryInsert = "INSERT INTO category (name, created, updated) " +
        "VALUES ('Test name ${i}','" + exampleTimestamp + "','" + exampleTimestamp + "');\n"

        dataSql.append(categoryInsert)
    }
}

generateCategories()