package contacts

import contacts.model.Menu

fun main(args: Array<String>) {
    val phoneBook = PhoneBook(args)

    menu@ while (true) {
        print("\n[menu] Enter action (add, list, search, count, exit): ")
        val userInput = readln().trim()

        when (Menu.entries.find { it.value == userInput }) {
            Menu.ADD -> phoneBook.addContact()
            Menu.LIST -> phoneBook.listContact()
            Menu.SEARCH -> phoneBook.searchContact()
            Menu.COUNT -> phoneBook.countContact()
            Menu.EXIT -> {
                phoneBook.close()
                break@menu
            }
            else -> println("Menu doesn't exist!")
        }
    }
}
