package presentation.petgallery

import android.os.Bundle
import android.view.View
import com.drodobyte.coreandroid.x.onBackPressed
import com.drodobyte.lostpet.R
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.pets_gallery_fragment.*
import util.AppFragment
import util.AppPresenter

class PetsGalleryFragment : AppFragment(), PetsGalleryPresenter.View {
    override fun layout() = R.layout.pets_gallery_fragment
    override fun presenter() = PetsGalleryPresenter(this, PetsGalleryModel(petCache), coordinator)

    private val onVisible = PublishSubject.create<Any>()
    private val clickedBack = PublishSubject.create<Any>()
    private val onClickedImage = PublishSubject.create<String>()
    private val adapter = PetsGalleryAdapter(onClickedImage::onNext)

    override fun onVisible() =
        onVisible

    override fun onClickedImage() =
        onClickedImage

    override fun onClickedBack() =
        clickedBack

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
        requireActivity().onBackPressed {
            clickedBack.onNext(Any())
        }
        gallery.adapter = adapter
        onVisible.onNext(Any())
    }
}
