package presentation.pet

import entity.Pet

sealed class PetViewState {

    object Loading : PetViewState()

    object Updated : PetViewState()

    object Saved : PetViewState()

    data class Ready(val pet: Pet) : PetViewState()

    data class Error(val error: Throwable) : PetViewState()
}