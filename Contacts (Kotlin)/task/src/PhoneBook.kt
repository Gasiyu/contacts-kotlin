package contacts

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import contacts.adapters.LocalDateTimeAdapter
import contacts.model.*
import contacts.utils.ACTION_BACK
import contacts.utils.GENDER_LIST
import java.io.File

class PhoneBook(args: Array<String>? = null) {
    private val contacts = mutableListOf<Contact>()
    private val file: File

    private val adapterFactory = PolymorphicJsonAdapterFactory
        .of(Contact::class.java, "contact")
        .withSubtype(Person::class.java, "person")
        .withSubtype(Organization::class.java, "organization")
    private val moshi = Moshi.Builder()
        .add(LocalDateTimeAdapter())
        .add(adapterFactory)
        .addLast(KotlinJsonAdapterFactory())
        .build()
    private val type = Types.newParameterizedType(MutableList::class.java, Contact::class.java)
    private val adapter = moshi.adapter<MutableList<Contact>>(type)

    init {
        val fileName = if (args?.size == 1) args.first() else DATABASE_NAME
        file = File(fileName)
        println("open $fileName")

        importDataFromDatabase()
    }

    private fun importDataFromDatabase() {
        try {
            val json = file.readText()
            val contacts = adapter.fromJson(json)
            contacts?.forEach { contact -> this.contacts.add(contact) }
        } catch (_: Exception) {}
    }

    private fun exportDataToDatabase() {
        val json = adapter.toJson(contacts)
        file.writeText(json)
    }

    fun countContact() {
        println("The Phone Book has ${contacts.size} records.")
    }

    fun searchContact() {
        print("Enter search query: ")
        val query = readln().trim()

        val result = contacts.filter {
            it.displayName.contains(query, true)  or it.number.contains(query, true)
        }
        println("Found ${result.size} results:")
        result.printList()

        print("\n[search] Enter action ([number], back, again): ")
        val action = readln().trim()

        when (SearchAction.entries.find { it.value == action }) {
            SearchAction.BACK -> return
            SearchAction.AGAIN -> searchContact()
            else -> action.toIntOrNull()?.let { number ->
                result.getOrNull(number - 1)?.let { contact ->
                    contact.printFullInfo()

                    modifyContact(contact)
                } ?: println("Wrong number!")
            } ?: println("Action doesn't exist!")
        }
    }

    private fun modifyContact(contact: Contact) {
        print("\n[record] Enter action (edit, delete, menu): ")
        val action = readln().trim()

        when (ContactAction.entries.find { it.value == action }) {
            ContactAction.EDIT -> editContact(contact)
            ContactAction.DELETE -> deleteContact(contact)
            ContactAction.MENU -> return
            else -> println("Action doesn't exist!")
        }
    }

    private fun editContact(contact: Contact) {
        print("Select a field (${contact.properties.joinToString(", ")}): ")
        val field = readln().trim()

        if (contact.properties.contains(field)) {
            print("Enter $field: ")
            val value = readln().trim()

            contact.setProperty(field, value)
            exportDataToDatabase().run { println("Saved") }
            contact.printFullInfo()
            modifyContact(contact)
        } else println("Property $field not found!")
    }

    private fun deleteContact(contact: Contact) {
        contacts.remove(contact)
        exportDataToDatabase().run { println("The record removed!") }
    }

    fun addContact() {
        print("Enter the type (person, organization): ")
        val type = readln().trim()

        when (ContactType.entries.find { it.value == type }) {
            ContactType.PERSON -> inputPerson()
            ContactType.ORGANIZATION -> inputOrganization()
            else -> println("Wrong type!")
        }
    }

    private fun inputPerson() {
        val contact = Person()

        print("Enter the name: ")
        val name = readln().trim()
        contact.setProperty(Person.PROPERTY_NAME, name)

        print("Enter the surname: ")
        val surname = readln().trim()
        contact.setProperty(Person.PROPERTY_SURNAME, surname)

        print("Enter the birth date: ")
        val birthDate = readln().trim()
        contact.setProperty(Person.PROPERTY_BIRTH_DATE, birthDate)

        print("Enter the gender (${GENDER_LIST.joinToString(", ")}): ")
        val gender = readln().trim()
        contact.setProperty(Person.PROPERTY_GENDER, gender)

        print("Enter the number: ")
        val number = readln().trim()
        contact.setProperty(Person.PROPERTY_NUMBER, number)

        contacts.add(contact)
        exportDataToDatabase().run { println("The record added.") }
    }

    private fun inputOrganization() {
        val organization = Organization()

        print("Enter the organization name: ")
        val name = readln().trim()
        organization.setProperty(Organization.PROPERTY_NAME, name)

        print("Enter the address: ")
        val address = readln().trim()
        organization.setProperty(Organization.PROPERTY_ADDRESS, address)

        print("Enter the number: ")
        val number = readln().trim()
        organization.setProperty(Organization.PROPERTY_NUMBER, number)

        contacts.add(organization)
        exportDataToDatabase().run { println("The record added.") }
    }

    fun listContact() {
        contacts.printList()

        print("\n[list] Enter action ([number], back): ")
        val action = readln().trim()

        if (action == ACTION_BACK) return

        action.toIntOrNull()?.let { number ->
            contacts.getOrNull(number - 1)?.let { contact ->
                contact.printFullInfo()

                modifyContact(contact)
            } ?: println("Wrong number!")
        } ?: println("Action doesn't exist!")
    }

    private fun Collection<Contact>.printList() = this.forEachIndexed { index, contact ->
        println("${index + 1}. ${contact.displayName}")
    }

    fun close() {
        if (file.exists()) file.delete()
    }

    companion object {
        private const val DATABASE_NAME = "phonebook.db"
    }
}
