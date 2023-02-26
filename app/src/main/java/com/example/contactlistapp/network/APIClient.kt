package com.example.contactlistapp.network

import com.example.contactlistapp.model.ContactInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface APIClient {
    @GET("users")
    suspend fun getContactList(): List<ContactInfo>

    @GET("users/{id}")
    suspend fun getContactDetails(
        @Path("id") number: Int
    ): ContactInfo
}