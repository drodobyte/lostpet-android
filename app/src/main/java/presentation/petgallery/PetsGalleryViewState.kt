package presentation.petgallery

sealed class PetsGalleryViewState {

    object Loading : PetsGalleryViewState()

    data class Ready(val urls: List<String>) : PetsGalleryViewState()

    object Saved : PetsGalleryViewState()

    data class Error(val error: Throwable) : PetsGalleryViewState()
}