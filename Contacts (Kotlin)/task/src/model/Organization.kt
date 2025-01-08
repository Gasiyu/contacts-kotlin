package contacts.model

import contacts.utils.DATE_FORMAT
import contacts.utils.NO_DATA_STRING
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Organization : Contact() {
    private var address: String = ""
    override val displayName: String
        get() = name
    override var updatedAt: LocalDateTime = LocalDateTime.now()
    override val properties: List<String> = listOf(PROPERTY_NAME, PROPERTY_ADDRESS, PROPERTY_NUMBER)

    override fun setProperty(property: String, value: String) = when (property) {
        PROPERTY_NAME -> name = value
        PROPERTY_ADDRESS -> address = value
        PROPERTY_NUMBER -> number = value
        else -> println("Unknown property!")
    }.run {
        updatedAt = LocalDateTime.now()
    }

    override fun getProperty(property: String): String = when (property) {
        PROPERTY_NAME -> name
        PROPERTY_ADDRESS -> address
        PROPERTY_NUMBER -> number
        else -> ""
    }.let { it.ifBlank { NO_DATA_STRING } }

    override fun printFullInfo() {
        println(
            """
            Organization name: ${getProperty(PROPERTY_NAME)}
            Address: ${getProperty(PROPERTY_ADDRESS)}
            Number: ${getProperty(PROPERTY_NUMBER)}
            Time created: ${createdAt.format(DateTimeFormatter.ofPattern(DATE_FORMAT))}
            Time last edit: ${updatedAt.format(DateTimeFormatter.ofPattern(DATE_FORMAT))}
        """.trimIndent()
        )
    }

    companion object {
        const val PROPERTY_NAME = "name"
        const val PROPERTY_ADDRESS = "address"
        const val PROPERTY_NUMBER = "number"
    }
}
