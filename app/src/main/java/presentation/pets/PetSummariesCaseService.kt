package presentation.pets

import case.ListPetSummariesCase

class PetSummariesCaseService(val case: ListPetSummariesCase) : PetSummariesPresenter.Service {

    override fun allPetsWithOneDummy() = case.listAllWithOneDummy()

    override fun foundPets() = case.listFound()

    override fun lostPets() = case.listLost()
}
