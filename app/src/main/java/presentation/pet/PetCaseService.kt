package presentation.pet

import case.SavePetCase
import case.ShowPetCase
import entity.Pet
import io.reactivex.Single

class PetCaseService(val showCase: ShowPetCase, val saveCase: SavePetCase) :
    PetService {

    override fun newPet() = Single.just(Pet(null))

    override fun pet(id: Long) = showCase.show(id)

    override fun save(pet: Pet) = saveCase.save(pet)
}
