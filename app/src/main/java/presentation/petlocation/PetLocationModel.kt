package presentation.petlocation

import com.drodobyte.coreandroid.util.Cache
import entity.Pet
import io.reactivex.Observable
import presentation.petlocation.PetLocationPresenter.Model
import presentation.petlocation.PetLocationPresenter.Pos
import presentation.petlocation.PetLocationViewState.Ready
import presentation.petlocation.PetLocationViewState.Updated
import util.toPos

class PetLocationModel(val currentPos: () -> Pos, val cache: Cache<Pet>) : Model {

    override fun petOrUserLocation() =
        Observable.just(Ready(getPos()) as PetLocationViewState)

    override fun updatePos(pos: Pos): Observable<PetLocationViewState> {
        cache.update { it.copy(location = it.location.copy(x = pos.x, y = pos.y, z = pos.z)) }
        return Observable.just(Updated(getPos()))
    }

    private fun getPos() =
        if (cache.isCleared() || cache.get().location.undefined)
            currentPos()
        else
            cache.get().location.toPos()
}
