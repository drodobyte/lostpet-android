package presentation.pets

import case.ListPetSummariesCase.PetSummary

sealed class PetsViewState {

    object Loading : PetsViewState()

    data class Ready(val pets: List<PetSummary>) : PetsViewState()

    data class Error(val error: Throwable) : PetsViewState()
}