package presentation.pets

import io.reactivex.Observable
import util.AppPresenter

class PetsPresenter(view: View, model: Model, coordinator: Coordinator) : AppPresenter() {
    init {
        view.onShowPets()
            .flatMap(model::pets)
            .subscribe(view::render)
            .add()

        view.onClickedPet()
            .subscribe(coordinator::onClickedPet)
            .add()

        view.onClickedNewPet()
            .subscribe { coordinator.onClickedNewPet() }
            .add()
    }

    interface View {
        fun onShowPets(): Observable<Filter>
        fun onClickedPet(): Observable<Long>
        fun onClickedNewPet(): Observable<Any>

        fun render(state: PetsViewState)
    }

    interface Model {
        fun pets(filter: Filter): Observable<PetsViewState>
    }

    interface Coordinator {
        fun onClickedNewPet()
        fun onClickedPet(id: Long)
    }

    enum class Filter {
        All, Found, Lost
    }
}
