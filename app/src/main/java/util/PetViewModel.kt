package util

import androidx.lifecycle.ViewModel
import entity.Pet

/**
 * Shared ViewModel when editing Pet (PetFragment and PetLocationFragment)
 */
class PetViewModel : ViewModel() {

    var pet: Pet = Pet(null)

    fun reset() {
        pet = Pet(null)
    }
}