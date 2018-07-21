package baktiyar.com.poputkakg.util

import baktiyar.com.poputkakg.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ForumService {

    @POST("users/create/")
    fun createUser(@Body user: User): Call<ProfileInfo>

    @GET("cities/")
    fun getCities(): Call<List<City>>

    @POST("users/validate/")
    fun checkSmsCode(@Body smsCode: SmsCode): Call<Token>

    @POST("users/login/")
    fun login(@Body login: Login): Call<Token>
}