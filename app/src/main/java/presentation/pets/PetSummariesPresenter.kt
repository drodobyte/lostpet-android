package presentation.pets

import case.ListPetSummariesCase.PetSummary
import io.reactivex.Observable
import presentation.pets.PetSummariesPresenter.Filter.*

class PetSummariesPresenter(view: View, service: Service) {
    init {
        view.visible {
            service.allPetsWithOneDummy().toList().subscribe(
                view::showSummaries
            )
        }
        view.clickedFilter {
            when (it) {
                All -> service.allPetsWithOneDummy()
                Found -> service.foundPets()
                Lost -> service.lostPets()
            }.toList().subscribe(
                view::showSummaries
            )
        }
        view.clickedNewPet {
            view.showPet(null)
        }
        view.clickedPetSummary {
            view.showPet(it)
        }
    }

    interface View {
        fun visible(action: () -> Unit)
        fun clickedFilter(action: (Filter) -> Unit)
        fun clickedPetSummary(action: (id: Long) -> Unit)
        fun clickedNewPet(action: () -> Unit)
        fun showSummaries(summaries: List<PetSummary>)
        fun showPet(id: Long?)
    }

    interface Service {
        fun allPetsWithOneDummy(): Observable<PetSummary>
        fun foundPets(): Observable<PetSummary>
        fun lostPets(): Observable<PetSummary>
    }

    enum class Filter {
        All, Found, Lost
    }
}
