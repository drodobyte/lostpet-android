package presentation.pets

import case.ListPetSummariesCase
import presentation.pets.PetsPresenter.Filter
import presentation.pets.PetsPresenter.Filter.*
import presentation.pets.PetsViewState.*
import service.PetService

class PetsModel(service: PetService) : PetsPresenter.Model {

    private val case = ListPetSummariesCase(service)

    override fun pets(filter: Filter) =
        when (filter) {
            All -> case.listAllWithOneDummy()
            Found -> case.listFound()
            Lost -> case.listLost()
        }
            .toList()
            .map { Ready(it) as PetsViewState }
            .onErrorReturn { Error(it) }
            .toObservable()
            .startWith(Loading)
}
