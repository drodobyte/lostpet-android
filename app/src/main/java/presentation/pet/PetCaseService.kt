package presentation.pet

import case.SavePetCase
import case.ShowPetCase
import entity.Pet
import io.reactivex.Single
import presentation.pet.PetPresenter.Service

class PetCaseService(val showCase: ShowPetCase, val saveCase: SavePetCase) : Service {

    override fun newPet() = Single.just(Pet(null))

    override fun pet(id: Long) = showCase.show(id)

    override fun save(pet: Pet) = saveCase.save(pet)
}
