package baktiyar.com.poputkakg.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Network {
    val REQUEST_TIME = 1000L

    fun initService(): ForumService {
        return Retrofit.Builder()
                .baseUrl(Const.URL)
                .addConverterFactory(initGson())
                .client(getClient())
                .build().create(ForumService::class.java)
    }

    private fun initGson(): GsonConverterFactory {
        return GsonConverterFactory.create(GsonBuilder().create())
    }

    private fun getClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.i(
                "_______________", message) })
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain.request()

                    val request = original.newBuilder()
                            .method(original.method(), original.body())
                            .build()
                    return@addInterceptor chain.proceed(request)

                }
                .addInterceptor(interceptor)
                .writeTimeout(REQUEST_TIME, TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIME, TimeUnit.SECONDS)
                .connectTimeout(REQUEST_TIME, TimeUnit.SECONDS)
        return client.build()
    }
}