package presentation.pet

import entity.Pet
import io.reactivex.Maybe
import io.reactivex.Single

class PetPresenter(view: View, service: Service, coordinator: Coordinator) {
    init {
        view.visible { editing ->
            if (editing)
                view.showPet(view.filledPet())
            else
                with(coordinator.clickedPetId()) {
                    when (this) {
                        null -> service.newPet().toMaybe()
                        else -> service.pet(this)
                    }.subscribe(view::showPet)
                }
        }
        view.clickedImage {
            coordinator.onClickedPetImage()
        }
        view.clickedMap {
            coordinator.onClickedPetLocation()
        }
        view.clickedBack {
            service.save(view.filledPet())
                .subscribe(
                    { coordinator.onClickedBack() },
                    { view.showErrorSave(it) }
                )
        }
    }

    interface View {
        fun visible(action: (editing: Boolean) -> Unit)
        fun clickedImage(action: (url: String) -> Unit)
        fun clickedMap(action: () -> Unit)
        fun clickedBack(action: () -> Unit)
        fun showPet(pet: Pet)
        fun showErrorSave(ex: Throwable)
        fun filledPet(): Pet
    }

    interface Service {
        fun newPet(): Single<Pet>
        fun pet(id: Long): Maybe<Pet>
        fun save(pet: Pet): Single<Pet>
    }

    interface Coordinator {
        fun clickedPetId(): Long?
        fun onClickedPetImage()
        fun onClickedPetLocation()
        fun onClickedBack()
    }
}
