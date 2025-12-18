package com.example.bchatbox

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers(
        "Authorization: key=10c8b7651166b316d8b154711a1dd1fb86322310", // Replace with your server key
        "Content-Type: application/json"
    )
    @POST("fcm/send")
    fun sendNotification(@Body sender: Sender): Call<MyResponse>
}