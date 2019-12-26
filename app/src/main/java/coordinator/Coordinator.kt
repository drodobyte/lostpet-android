package coordinator

import presentation.pet.PetPresenter
import presentation.petgallery.PetsGalleryPresenter
import presentation.petlocation.PetLocationPresenter
import presentation.pets.PetSummariesPresenter

interface Coordinator :
    PetSummariesPresenter.Coordinator,
    PetPresenter.Coordinator,
    PetLocationPresenter.Coordinator,
    PetsGalleryPresenter.Coordinator