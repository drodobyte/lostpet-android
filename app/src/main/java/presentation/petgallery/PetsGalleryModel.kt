package presentation.petgallery

import com.drodobyte.coreandroid.util.Cache
import com.drodobyte.coreandroid.x.onIO
import entity.Pet
import io.reactivex.Observable
import io.reactivex.Single
import presentation.petgallery.PetsGalleryPresenter.Model
import presentation.petgallery.PetsGalleryViewState.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class PetsGalleryModel(private val cache: Cache<Pet>) : Model {

    override fun imageUrls(): Observable<PetsGalleryViewState> =
        DogRestApi.instance.randomImageUrls(10)
            .map { Ready(it.message) as PetsGalleryViewState }
            .onErrorReturn { Error(it) }
            .toObservable()
            .startWith(Loading)
            .onIO()

    override fun saveImageUrl(url: String): Observable<PetsGalleryViewState> {
        cache.update { it.copy(imageUrl = url) }
        return Observable.just(Saved)
    }

    private interface DogRestApi {

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
}