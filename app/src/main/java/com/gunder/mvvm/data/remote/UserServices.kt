package com.gunder.mvvm.data.remote

import retrofit2.http.GET

interface UserServices {
    @GET("/users")
    suspend fun fetchUser(): List<User>
}