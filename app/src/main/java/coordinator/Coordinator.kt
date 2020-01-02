package coordinator

import presentation.pet.PetPresenter
import presentation.petgallery.PetsGalleryPresenter
import presentation.petlocation.PetLocationPresenter
import presentation.pets.PetsPresenter

interface Coordinator :
    PetsPresenter.Coordinator,
    PetPresenter.Coordinator,
    PetLocationPresenter.Coordinator,
    PetsGalleryPresenter.Coordinator