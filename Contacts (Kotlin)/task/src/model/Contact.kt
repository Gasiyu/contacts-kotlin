package contacts.model

import java.time.LocalDateTime

abstract class Contact {

    abstract val displayName: String
    abstract val properties: List<String>
    abstract var updatedAt: LocalDateTime

    abstract fun setProperty(property: String, value: String)
    abstract fun getProperty(property: String): String
    abstract fun printFullInfo()

    var name: String = ""
    var number: String = ""
        set(value) {
            field = if (isPhoneNumberValid(value)) {
                value
            } else {
                println("Wrong number format!")
                ""
            }
        }
    val createdAt: LocalDateTime = LocalDateTime.now()

    private fun isPhoneNumberValid(phone: String): Boolean = PHONE_NUMBER_REGEX.matches(phone)

    companion object {
        private val PHONE_NUMBER_REGEX =
            """^(\+\w+)(\s)(\(?\w+\)?)((\s?|-)\w+)+|^(\w+)((\s?|-?)\(?\w{2,}\)?)+|^(\(\w+\))((\s?|-?)\w{2,})+|^(\+)?(\(?\w+\)?)""".toRegex()
    }
}
