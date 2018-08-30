package baktiyar.com.poputkakg.util

import baktiyar.com.poputkakg.model.*
import okhttp3.RequestBody
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
    fun getProfileInfo(@Path("user_id") user_id: Int, @Header("Authorization") token: String): Call<ProfileInfo>

    @Headers("Content-Type:application/json")
    @POST("routes/")
    fun createRout(@Body rout: Rout, @Header("Authorization") token: String): Call<Rout>

    @Headers("Content-Type:application/json")
    @GET("routes/")
    fun getRoutes(@Header("Authorization") token: String): Call<List<Rout>>

    @POST("fcm/devices/")
    fun sendFirebaseToken(@Header("Authorization") token: String, @Body file: RequestBody): Call<TokenInfo>

    @Headers("Content-Type:application/json")
    @POST("deals/")
    fun offerDeal(@Header("Authorization") token: String,@Path("route")
    routeId:Int?,@Path("seats")seats:Int?): Call<DealBody>
}