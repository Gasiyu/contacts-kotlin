package contacts.model

import contacts.utils.DATE_FORMAT
import contacts.utils.GENDER_LIST
import contacts.utils.NO_DATA_STRING
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class Person : Contact() {
    private var surname: String = ""
    private var birthDate: String = ""
        set(value) {
            field = try {
                val date = LocalDate.parse(value)
                date.toString()
            } catch (ex: DateTimeParseException) {
                println("Bad birth date!")
                ""
            }
        }
    private var gender: String = ""
        set(value) {
            field = if (value in GENDER_LIST) {
                value
            } else {
                println("Bad gender!")
                ""
            }
        }
    override val displayName: String
        get() = "$name $surname"
    override var updatedAt: LocalDateTime = LocalDateTime.now()
    override val properties: List<String> =
        listOf(PROPERTY_NAME, PROPERTY_SURNAME, PROPERTY_BIRTH_DATE, PROPERTY_GENDER, PROPERTY_NUMBER)

    override fun setProperty(property: String, value: String) = when (property) {
        PROPERTY_NAME -> name = value
        PROPERTY_SURNAME -> surname = value
        PROPERTY_GENDER -> gender = value
        PROPERTY_NUMBER -> number = value
        PROPERTY_BIRTH_DATE -> birthDate = value
        else -> println("Unknown property!")
    }.run {
        updatedAt = LocalDateTime.now()
    }

    override fun getProperty(property: String): String = when (property) {
        PROPERTY_NAME -> name
        PROPERTY_SURNAME -> surname
        PROPERTY_GENDER -> gender
        PROPERTY_NUMBER -> number
        PROPERTY_BIRTH_DATE -> birthDate
        else -> ""
    }.let { it.ifBlank { NO_DATA_STRING } }

    override fun printFullInfo() {
        println("""
            Name: ${getProperty(PROPERTY_NAME)}
            Surname: ${getProperty(PROPERTY_SURNAME)}
            Birth date: ${getProperty(PROPERTY_BIRTH_DATE)}
            Gender: ${getProperty(PROPERTY_GENDER)}
            Number: ${getProperty(PROPERTY_NUMBER)}
            Time created: ${createdAt.format(DateTimeFormatter.ofPattern(DATE_FORMAT))}
            Time last edit: ${updatedAt.format(DateTimeFormatter.ofPattern(DATE_FORMAT))}
        """.trimIndent())
    }

    companion object {
        const val PROPERTY_NAME = "name"
        const val PROPERTY_SURNAME = "surname"
        const val PROPERTY_BIRTH_DATE = "birth"
        const val PROPERTY_GENDER = "gender"
        const val PROPERTY_NUMBER = "number"
    }
}
