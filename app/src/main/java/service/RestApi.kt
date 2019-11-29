package service

import entity.Pet
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.*

internal object RestApi : PetService {

    override fun pets() = api.pets().flatMapIterable { it.data }!!

    override fun pet(id: Long) = api.pet(id)

    override fun save(pet: Pet) =
        if (pet.isNew()) api.save(pet) else api.update(pet, pet.id!!)


    private val api: Api by lazy {
        RetrofitRemRestApiBuilder.create(Api::class.java)
    }

    private interface Api {

        @GET("pets")
        fun pets(): Observable<PetsData>

        @GET("pets/{id}")
        fun pet(@Path("id") id: Long): Maybe<Pet>

        @POST("pets")
        fun save(@Body pet: Pet): Single<Pet>

        @PUT("pets/{id}")
        fun update(@Body pet: Pet, @Path("id") id: Long): Single<Pet>
    }

    private data class PetsData(val data: List<Pet>)
}

