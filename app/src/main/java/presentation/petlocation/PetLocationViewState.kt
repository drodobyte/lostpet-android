package presentation.petlocation

import presentation.petlocation.PetLocationPresenter.Pos

sealed class PetLocationViewState {

    object Loading : PetLocationViewState()

    data class Ready(val pos: Pos) : PetLocationViewState()

    data class Updated(val pos: Pos) : PetLocationViewState()
}
