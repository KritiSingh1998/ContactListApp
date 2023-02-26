package com.example.contactlistapp.model


data class ContactInfo(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val address: Address,
    val phone: String,
    val website: String,
    val company: Company
) : java.io.Serializable {
}

data class ContactList(val contactList: List<ContactList>)
data class Address(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val geo: Geo
) {
    fun getCompleteAddress(): String {
        return "${street}, ${suite}, ${city}, ${zipcode}"
    }
}

data class Geo(val lat: String, val lng: String)

data class Company(val name: String, val catchPhrase: String, val bs: String) {
    fun getCompleteCompany(): String {
        return "${name}, ${catchPhrase}, ${bs}"
    }
}