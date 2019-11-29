package presentation.petgallery

import android.os.Bundle
import android.view.View
import com.drodobyte.lostpet.R
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.pets_gallery_fragment.*
import util.AppFragment
import util.onBackPressed
import util.onIO

class PetsGalleryFragment : AppFragment(), PetsGalleryView, PetsGalleryService {
    override fun layout() = R.layout.pets_gallery_fragment

    private val visible = PublishSubject.create<Any>()
    private val clickedBack = PublishSubject.create<Any>()
    private val clickedImage = PublishSubject.create<String>()
    private val adapter = PetsGalleryAdapter(clickedImage::onNext)

    override fun visible(action: () -> Unit) =
        visible.xSubscribe(action)

    override fun clickedBack(action: () -> Unit) =
        clickedBack.xSubscribe(action)

    override fun goBack() =
        go.back()

    override fun showImages(urls: List<String>) {
        adapter + urls
    }

    override fun clickedImage(action: (url: String) -> Unit) {
        clickedImage.xSubscribe(action)
    }

    override fun imageUrls(): Observable<String> = DogRestApi.instance
        .randomImageUrls(10)
        .map { it.message }
        .flatMapObservable { Observable.fromIterable(it) }
        .onIO()

    override fun saveImageUrl(url: String): Completable {
        petViewModel.pet = petViewModel.pet.copy(imageUrl = url)
        return Completable.complete()
    }

    override fun onViewCreated(view: View, saved: Bundle?) {
        PetsGalleryPresenter(this, this)
        requireActivity().onBackPressed {
            clickedBack.onNext(Any())
        }
        gallery.adapter = adapter
        visible.onNext(Any())
    }
}
