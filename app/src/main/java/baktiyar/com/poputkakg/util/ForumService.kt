package baktiyar.com.poputkakg.util

import baktiyar.com.poputkakg.model.*
import retrofit2.Call
import retrofit2.http.*

interface ForumService {

    @POST("users/create/")
    fun createUser(@Body user: User): Call<ProfileInfo>

    @GET("cities/")
    fun getCities(): Call<List<City>>

    @POST("users/validate/")
    fun checkSmsCode(@Body smsCode: SmsCode): Call<Token>

    @POST("users/login/")
    fun login(@Body login: Login): Call<Token>

    @Headers("Content-Type:application/json")
    @GET("users/{user_id}/")
    fun getProfileInfo(@Path("user_id" )user_id: Int, @Header("Authorization") token :String) : Call<ProfileInfo>

    @Headers("Content-Type:application/json")
    @POST("routes/")
    fun createRout(@Body rout: Rout, @Header("Authorization") token :String) : Call<Rout>


}