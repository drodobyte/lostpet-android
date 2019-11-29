package presentation.petgallery

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

internal interface DogRestApi {

    @GET("breeds/image/random/{count}")
    fun randomImageUrls(@Path("count") count: Int): Single<ImageUrls>

    data class ImageUrls(val message: List<String>)

    companion object {
        var instance: DogRestApi = Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(DogRestApi::class.java)
    }
}