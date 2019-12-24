package presentation.petgallery

import io.reactivex.Completable
import io.reactivex.Observable

class PetsGalleryPresenter(view: View, service: Service) {
    init {
        view.visible {
            service.imageUrls().toList()
                .subscribe(view::showImages)
        }
        view.clickedImage {
            service.saveImageUrl(it).subscribe {
                view.goBack()
            }
        }
        view.clickedBack {
            view.goBack()
        }
    }

    interface View {
        fun visible(action: () -> Unit)
        fun showImages(urls: List<String>)
        fun clickedImage(action: (url: String) -> Unit)
        fun clickedBack(action: () -> Unit)
        fun goBack()
    }

    interface Service {
        fun imageUrls(): Observable<String>
        fun saveImageUrl(url: String): Completable
    }
}