package presentation.pet

import case.SavePetCase
import case.ShowPetCase
import com.drodobyte.coreandroid.util.Cache
import entity.Pet
import io.reactivex.Maybe
import io.reactivex.Observable
import presentation.pet.PetViewState.*
import service.PetService
import java.util.*

class PetModel(petService: PetService, val cache: Cache<Pet>) : PetPresenter.Model {

    private val saveCase = SavePetCase(petService)
    private val showCase = ShowPetCase(petService)

    override fun pet(id: Long?) =
        getPet(id)
            .doOnEvent { pet, _ -> cache.put(pet) }
            .map { Ready(it) as PetViewState }
            .onErrorReturn { Error(it) }
            .toObservable()
            .startWith(Loading)

    override fun updateName(name: String) =
        update { it.copy(name = name) }

    override fun updateDescription(description: String) =
        update { it.copy(description = description) }

    override fun updateFound(found: Boolean) =
        update { it.copy(found = found) }

    override fun updateDate(date: Date) =
        update { it.copy(location = cache.get().location.copy(date = date)) }

    override fun save(): Observable<PetViewState> =
        saveCase.save(cache.get())
            .doOnSuccess { cache.clear() }
            .toObservable()
            .map { Saved as PetViewState }
            .onErrorReturn { Error(it) }

    private fun getPet(id: Long?) =
        if (cache.isCleared())
            if (id == null) Maybe.just(Pet(null)) else showCase.show(id)
        else
            Maybe.just(cache.get())

    private fun update(updater: (pet: Pet) -> Pet) =
        Observable.just(cache.update(updater))
            .map { Updated as PetViewState }
}
