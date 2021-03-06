package presentation.pet

import android.os.Bundle
import android.view.View
import case.Checker.Error.ImageEmpty
import case.Checker.Error.NameEmpty
import case.SavePetCase
import case.ShowPetCase
import com.drodobyte.core.kotlin.check.Check.Ex
import com.drodobyte.coreandroid.x.onBackPressed
import com.drodobyte.coreandroid.x.xDate
import com.drodobyte.coreandroid.x.xFormatted
import com.drodobyte.coreandroid.x.xShow
import com.drodobyte.lostpet.R
import coordinator.Coordinator
import entity.Pet
import io.reactivex.subjects.PublishSubject.create
import kotlinx.android.synthetic.main.pet_fragment.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import service.PetService
import util.AppFragment
import util.xLoadPet
import util.xShowDialog

class PetFragment : AppFragment(), PetPresenter.View {

    override fun layout(): Int = R.layout.pet_fragment

    private val petService: PetService by inject()
    private val coordinator: Coordinator by inject { parametersOf(this) }
    private val clickedImage = create<String>()
    private val clickedMap = create<Any>()
    private val clickedBack = create<Any>()
    private val visiblePet = create<Boolean>()

    override fun visible(action: (Boolean) -> Unit) {
        visiblePet.xSubscribe(action)
    }

    override fun showPet(pet: Pet) = with(pet) {
        petViewModel.pet = copy()
        pet_name.setText(name)
        pet_image.xLoadPet(imageUrl)
        pet_found_icon.xShow(found)
        pet_found.isChecked = found
        pet_description.setText(description)
        pet_location_date.xDate(location.date)
        pet_touch_image_message.xShow(imageUrl.isBlank())
    }

    override fun clickedImage(action: (url: String) -> Unit) =
        clickedImage.xSubscribe(action)

    override fun clickedMap(action: () -> Unit) =
        clickedMap.xSubscribe(action)

    override fun clickedBack(action: () -> Unit) =
        clickedBack.xSubscribe(action)

    override fun showErrorSave(ex: Throwable) {
        if (ex is Ex)
            when {
                NameEmpty in ex.errors -> pet_name.error = "Name cannot be empty"
                ImageEmpty in ex.errors -> showError("Image cannot be empty")
            }
        else
            showError("Undefined error saving pet!")
    }

    override fun filledPet() = Pet(
        petViewModel.pet.id,
        pet_name.text.toString(),
        pet_description.text.toString(),
        petViewModel.pet.imageUrl,
        pet_found.isChecked,
        petViewModel.pet.location
    )

    override fun onViewCreated(view: View, saved: Bundle?) {
        PetPresenter(
            this,
            PetCaseService(
                ShowPetCase(petService),
                SavePetCase(petService)
            ),
            coordinator
        )
        pet_image.setOnClickListener {
            clickedImage.onNext(petViewModel.pet.imageUrl)
        }
        pet_location_pin.setOnClickListener {
            clickedMap.onNext(Any())
        }
        pet_location_date.setOnClickListener {
            petViewModel.pet.location.date.xShowDialog(fragmentManager) {
                pet_location_date.text = it.xFormatted()
            }
        }
        pet_found.setOnCheckedChangeListener { _, checked ->
            pet_found_icon.xShow(checked)
        }
        requireActivity().onBackPressed {
            clickedBack.onNext(Any())
        }
        visiblePet.onNext(!petViewModel.pet.undefined)
    }
}
