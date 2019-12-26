package coordinator

import android.os.Bundle
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import presentation.pet.PetFragmentArgs
import presentation.pet.PetFragmentDirections
import presentation.pets.PetsFragmentDirections
import util.AppFragment

internal class Go(private val fragment: AppFragment) {

    val args: Arg by lazy {
        Arg(fragment.arguments!!)
    }

    fun pet(id: Long?) =
        go(PetsFragmentDirections.actionPetsToPet(id?.let { longArrayOf(it) }))

    fun petGallery() {
        go(PetFragmentDirections.actionPetToGallery())
    }

    fun petLocation() = go(PetFragmentDirections.actionPetToLocation())

    fun back() {
        fragment.findNavController().popBackStack()
    }

    class Arg(private val arguments: Bundle) {
        fun pet(): Long? =
            PetFragmentArgs.fromBundle(arguments).id?.get(0)
    }

    private fun go(directions: NavDirections) =
        fragment.findNavController().navigate(directions)
}
