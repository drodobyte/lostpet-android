package presentation.petlocation

import io.reactivex.Observable
import util.AppPresenter

class PetLocationPresenter(view: View, model: Model, coordinator: Coordinator) : AppPresenter() {
    init {
        view.onVisible()
            .flatMap { model.petOrUserLocation() }
            .subscribe(view::render)
            .add()

        view.onPosChanged()
            .flatMap(model::updatePos)
            .subscribe(view::render)
            .add()

        view.onClickedBack()
            .map { coordinator.onClickedBack() }
            .subscribe()
            .add()
    }

    interface View {
        fun onVisible(): Observable<Any>
        fun onPosChanged(): Observable<Pos>
        fun onClickedBack(): Observable<Any>

        fun render(state: PetLocationViewState)
    }

    interface Model {
        fun petOrUserLocation(): Observable<PetLocationViewState>
        fun updatePos(pos: Pos): Observable<PetLocationViewState>
    }

    interface Coordinator {
        fun onClickedBack()
    }

    data class Pos(val x: Double, val y: Double, val z: Double)
}
