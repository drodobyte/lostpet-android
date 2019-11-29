package service

import android.content.Context
import entity.Pet
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import util.onIO

class AndroidPetService(context: Context) : PetService {

    private val rest: RestApi by lazy {
        RestApi
    }
    private val db: DbApi by lazy {
        DbApi.context = context
        DbApi
    }

    override fun pets(): Observable<Pet> {
        return rest.pets().onIO()
    }

    override fun pet(id: Long): Maybe<Pet> {
        return rest.pet(id).onIO()
    }

    override fun save(pet: Pet): Single<Pet> {
        return rest.save(pet).onIO()
    }
}