package presentation.petgallery

import android.os.Bundle
import android.view.View
import com.drodobyte.coreandroid.x.onBackPressed
import com.drodobyte.coreandroid.x.onIO
import com.drodobyte.lostpet.R
import coordinator.Coordinator
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.pets_gallery_fragment.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import presentation.petgallery.PetsGalleryPresenter.Service
import util.AppFragment

class PetsGalleryFragment : AppFragment(), PetsGalleryPresenter.View, Service {
    override fun layout() = R.layout.pets_gallery_fragment

    private val coordinator: Coordinator by inject { parametersOf(this) }
    private val visible = PublishSubject.create<Any>()
    private val clickedBack = PublishSubject.create<Any>()
    private val clickedImage = PublishSubject.create<String>()
    private val adapter = PetsGalleryAdapter(clickedImage::onNext)

    override fun visible(action: () -> Unit) =
        visible.xSubscribe(action)

    override fun clickedBack(action: () -> Unit) =
        clickedBack.xSubscribe(action)

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
        PetsGalleryPresenter(this, this, coordinator)
        requireActivity().onBackPressed {
            clickedBack.onNext(Any())
        }
        gallery.adapter = adapter
        visible.onNext(Any())
    }
}
