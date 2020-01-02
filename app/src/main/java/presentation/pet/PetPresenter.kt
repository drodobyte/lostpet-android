package presentation.pet

import io.reactivex.Observable
import presentation.pet.PetViewState.Error
import util.AppPresenter
import java.util.*

class PetPresenter(view: View, model: Model, coordinator: Coordinator) : AppPresenter() {
    init {
        view.onShowPet()
            .flatMap { model.pet(coordinator.petId()) }
            .subscribe(view::render)
            .add()

        Observable.merge(
            view.onNameChanged().flatMap(model::updateName),
            view.onDescriptionChanged().flatMap(model::updateDescription),
            view.onFoundChanged().flatMap(model::updateFound),
            view.onDateChanged().flatMap(model::updateDate)
        ).subscribe(view::render)
            .add()

        view.onClickedImage()
            .subscribe { coordinator.onClickedPetImage() }
            .add()

        view.onClickedMap()
            .subscribe { coordinator.onClickedPetLocation() }
            .add()

        view.onClickedBack()
            .flatMap { model.save() }
            .subscribe {
                if (it is Error)
                    view.render(it)
                else
                    coordinator.onClickedBack()
            }.add()
    }

    interface View {
        fun onShowPet(): Observable<Any>
        fun onNameChanged(): Observable<String>
        fun onDescriptionChanged(): Observable<String>
        fun onFoundChanged(): Observable<Boolean>
        fun onDateChanged(): Observable<Date>
        fun onClickedImage(): Observable<Any>
        fun onClickedMap(): Observable<Any>
        fun onClickedBack(): Observable<Any>

        fun render(state: PetViewState)
    }

    interface Model {
        fun pet(id: Long?): Observable<PetViewState>
        fun updateName(name: String): Observable<PetViewState>
        fun updateDescription(description: String): Observable<PetViewState>
        fun updateFound(found: Boolean): Observable<PetViewState>
        fun updateDate(date: Date): Observable<PetViewState>
        fun save(): Observable<PetViewState>
    }

    interface Coordinator {
        fun petId(): Long?
        fun onClickedPetImage()
        fun onClickedPetLocation()
        fun onClickedBack()
    }
}
