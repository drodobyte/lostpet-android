package presentation.petgallery

import android.os.Bundle
import android.view.View
import com.drodobyte.coreandroid.x.backPressObservable
import com.drodobyte.lostpet.R
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.pets_gallery_fragment.*
import util.AppFragment
import util.AppPresenter

class PetsGalleryFragment : AppFragment(), PetsGalleryPresenter.View {
    override fun layout() = R.layout.pets_gallery_fragment
    override fun presenter() = PetsGalleryPresenter(this, PetsGalleryModel(petCache), coordinator)

    private val onVisible = PublishSubject.create<Any>()
    private val adapter = PetsGalleryAdapter()

    override fun onVisible() =
        onVisible

    override fun onClickedImage() =
        adapter.onClickObservable()

    override fun onClickedBack() =
        requireActivity().backPressObservable()

    override fun render(state: PetsGalleryViewState) {
        when (state) {
            is PetsGalleryViewState.Loading -> renderLoading()
            is PetsGalleryViewState.Ready -> renderImages(state.urls)
            is PetsGalleryViewState.Error -> renderError(state.error)
        }
    }

    private fun renderLoading() {}

    private fun renderImages(urls: List<String>) {
        adapter + urls
    }

    private fun renderError(error: Throwable) {
        showError("Error loading images")
    }

    override fun onViewCreated(view: View, saved: Bundle?, presenter: AppPresenter) {
        gallery.adapter = adapter
        onVisible.onNext(Any())
    }
}
