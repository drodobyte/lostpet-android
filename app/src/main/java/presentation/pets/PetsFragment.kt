package presentation.pets

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import app.Container
import case.ListPetSummariesCase
import case.ListPetSummariesCase.PetSummary
import com.drodobyte.coreandroid.x.onBackPressed
import com.drodobyte.lostpet.R
import io.reactivex.subjects.PublishSubject.create
import kotlinx.android.synthetic.main.pets_fragment.*
import presentation.pets.PetSummariesPresenter.Filter
import util.AppFragment

class PetsFragment : AppFragment(), PetSummariesPresenter.View {
    override fun layout(): Int = R.layout.pets_fragment
    override fun menu(): Int = R.menu.options

    private val visible = create<Any>()
    private val clickedFilter = create<Filter>()
    private val clickedPetSummary = create<Long>()
    private val clickedNewPet = create<Any>()
    private lateinit var adapter: PetsAdapter

    override fun visible(action: () -> Unit) {
        visible.xSubscribe(action)
    }

    override fun clickedFilter(action: (Filter) -> Unit) {
        clickedFilter.xSubscribe(action)
    }

    override fun clickedPetSummary(action: (id: Long) -> Unit) {
        clickedPetSummary.xSubscribe(action)
    }

    override fun clickedNewPet(action: () -> Unit) {
        clickedNewPet.xSubscribe(action)
    }

    override fun showSummaries(summaries: List<PetSummary>) {
        adapter + summaries
    }

    override fun showPet(id: Long?) {
        go.pet(id)
    }

    override fun onViewCreated(v: View, saved: Bundle?) {
        PetSummariesPresenter(
            this,
            PetSummariesCaseService(ListPetSummariesCase(Container.petService))
        )
        adapter = PetsAdapter { clickedPetSummary.onNext(it.id!!) }
        petsView.adapter = adapter
        add_pet.setOnClickListener { clickedNewPet.onNext(Any()) }
        requireActivity().onBackPressed { activity?.finish() }
        visible.onNext(Any())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        clickedFilter.onNext(
            when (item.itemId) {
                R.id.all -> Filter.All
                R.id.found -> Filter.Found
                else -> Filter.Lost
            }
        )
        item.isChecked = true
        return true
    }
}
