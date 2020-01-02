package presentation.petgallery

import io.reactivex.Observable
import util.AppPresenter

class PetsGalleryPresenter(view: View, model: Model, coordinator: Coordinator) : AppPresenter() {
    init {
        view.onVisible()
            .flatMap { model.imageUrls() }
            .subscribe(view::render)
            .add()

        view.onClickedImage()
            .flatMap(model::saveImageUrl)
            .subscribe {
                if (it is PetsGalleryViewState.Error)
                    view.render(it)
                else
                    coordinator.onClickedGalleryImage()
            }.add()

        view.onClickedBack()
            .map { coordinator.onClickedBack() }
            .subscribe()
            .add()
    }

    interface View {
        fun onVisible(): Observable<Any>
        fun onClickedImage(): Observable<String>
        fun onClickedBack(): Observable<Any>

        fun render(state: PetsGalleryViewState)
    }

    interface Model {
        fun imageUrls(): Observable<PetsGalleryViewState>
        fun saveImageUrl(url: String): Observable<PetsGalleryViewState>
    }

    interface Coordinator {
        fun onClickedGalleryImage()
        fun onClickedBack()
    }
}