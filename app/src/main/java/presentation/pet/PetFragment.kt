package presentation.pet

import android.os.Bundle
import android.view.View
import case.Checker.Error.ImageEmpty
import case.Checker.Error.NameEmpty
import com.drodobyte.core.kotlin.check.Check.Ex
import com.drodobyte.coreandroid.x.asDate
import com.drodobyte.coreandroid.x.fromDate
import com.drodobyte.coreandroid.x.onBackPressed
import com.drodobyte.coreandroid.x.show
import com.drodobyte.lostpet.R
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import entity.Pet
import io.reactivex.subjects.PublishSubject.create
import kotlinx.android.synthetic.main.pet_fragment.*
import presentation.pet.PetViewState.*
import util.AppFragment
import util.AppPresenter
import util.onClickRunDateDialog
import util.xLoadPet
import java.util.concurrent.TimeUnit.SECONDS

class PetFragment : AppFragment(), PetPresenter.View {

    override fun layout(): Int = R.layout.pet_fragment
    override fun presenter() = PetPresenter(this, PetModel(petService, petCache), coordinator)

    private val onShowPet = create<Any>()
    private val onClickedBack = create<Any>()

    override fun onShowPet() =
        onShowPet

    override fun onNameChanged() =
        RxTextView.textChanges(pet_name)
            .debounce(1, SECONDS)
            .map { it.toString() }

    override fun onDescriptionChanged() =
        RxTextView.textChanges(pet_description)
            .debounce(1, SECONDS)
            .map { it.toString() }

    override fun onFoundChanged() =
        RxView.clicks(pet_found)
            .map { pet_found.isChecked }

    override fun onDateChanged() =
        RxTextView.textChanges(pet_location_date)
            .map { it.asDate() }

    override fun onClickedImage() =
        RxView.clicks(pet_image)

    override fun onClickedMap() =
        RxView.clicks(pet_location_pin)

    override fun onClickedBack() =
        onClickedBack

    override fun render(state: PetViewState) {
        when (state) {
            is Loading -> renderLoading()
            is Ready -> renderPet(state.pet)
            is Error -> renderError(state.error)
        }
    }

    override fun onViewCreated(view: View, saved: Bundle?, presenter: AppPresenter) {
        pet_location_date.onClickRunDateDialog(fragmentManager!!)
        pet_found.setOnCheckedChangeListener { _, checked ->
            pet_found_icon.show(checked)
        }
        requireActivity().onBackPressed {
            onClickedBack.onNext(Any())
        }
        onShowPet.onNext(Any())
    }

    private fun renderLoading() {}

    private fun renderPet(pet: Pet) = with(pet) {
        pet_name.setText(name)
        pet_image.xLoadPet(imageUrl)
        pet_found_icon.show(found)
        pet_found.isChecked = found
        pet_description.setText(description)
        pet_location_date.fromDate(location.date)
        pet_touch_image_message.show(imageUrl.isBlank())
    }

    private fun renderError(ex: Throwable) {
        if (ex is Ex) when {
            NameEmpty in ex.errors -> pet_name.error = "Name cannot be empty"
            ImageEmpty in ex.errors -> showError("Image cannot be empty")
        } else
            showError("Undefined error saving pet!")
    }
}
