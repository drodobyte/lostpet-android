package presentation.pets

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import case.ListPetSummariesCase.PetSummary
import com.drodobyte.coreandroid.x.onBackPressed
import com.drodobyte.lostpet.R
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.subjects.PublishSubject.create
import kotlinx.android.synthetic.main.pets_fragment.*
import presentation.pets.PetsPresenter.Filter
import presentation.pets.PetsPresenter.Filter.*
import presentation.pets.PetsViewState.*
import util.AppFragment
import util.AppPresenter

class PetsFragment : AppFragment(), PetsPresenter.View {
    override fun layout(): Int = R.layout.pets_fragment
    override fun menu(): Int = R.menu.options
    override fun presenter() = PetsPresenter(this, PetsModel(petService), coordinator)

    private val adapter = PetsAdapter { onClickedPet.onNext(it.id!!) }
    private val onShowPets = create<Filter>()
    private val onClickedPet = create<Long>()

    override fun onShowPets() =
        onShowPets

    override fun onClickedNewPet() =
        RxView.clicks(add_pet)

    override fun onClickedPet() =
        onClickedPet

    override fun render(state: PetsViewState) {
        when (state) {
            is Loading -> renderLoading()
            is Ready -> renderPets(state.pets)
            is Error -> renderError(state.error)
        }
    }

    private fun renderLoading() {}

    private fun renderPets(pets: List<PetSummary>) =
        adapter + pets

    private fun renderError(error: Throwable) =
        showError("Error loading pets")

    override fun onViewCreated(view: View, saved: Bundle?, presenter: AppPresenter) {
        petsView.adapter = adapter
        requireActivity().onBackPressed { activity?.finish() }
        onShowPets.onNext(All)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onShowPets.onNext(
            when (item.itemId) {
                R.id.all -> All
                R.id.found -> Found
                else -> Lost
            }
        )
        item.isChecked = true
        return true
    }
}
