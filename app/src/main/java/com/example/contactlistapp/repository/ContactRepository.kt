package com.example.contactlistapp.repository

import com.example.contactlistapp.model.ContactInfo
import com.example.contactlistapp.network.APIClient
import javax.inject.Inject

class ContactRepository constructor(val apiClient: APIClient) {
    suspend fun getAllContacts(): List<ContactInfo> {
        return apiClient.getContactList()
    }

    suspend fun getContactDetails(id: Int): ContactInfo {
        return apiClient.getContactDetails(id)
    }
}