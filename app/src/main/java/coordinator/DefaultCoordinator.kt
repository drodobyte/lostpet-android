package coordinator

import util.AppFragment

internal class DefaultCoordinator(fragment: AppFragment) : Coordinator {

    private var go: Go = Go(fragment)

    override fun onClickedNewPet() =
        go.pet(null)

    override fun onClickedPet(id: Long) =
        go.pet(id)

    override fun clickedPetId(): Long? =
        go.args.pet()

    override fun onClickedPetImage() =
        go.petGallery()

    override fun onClickedPetLocation() =
        go.petLocation()

    override fun onClickedGalleryImage() =
        go.back()

    override fun onClickedBack() =
        go.back()
}