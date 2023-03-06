package service

import android.util.Log
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy

internal object RetrofitRemRestApiBuilder {

    fun <T> create(clazz: Class<T>): T = Retrofit.Builder()
        .baseUrl("https://rem.dbwebb.se/api/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build().create(clazz)

    private val logger = object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            Log.i("retrofit", message)
        }
    }

    private val interceptor = HttpLoggingInterceptor(logger).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val cookies = JavaNetCookieJar(CookieManager().apply {
        setCookiePolicy(CookiePolicy.ACCEPT_ALL)
    })

    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .cookieJar(cookies)
        .build()
}
