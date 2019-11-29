package presentation.petlocation

import entity.Location
import java.util.*

class PetLocationPresenter(view: PetLocationView, service: PetLocationService) {
    init {
        view.visible {
            with(service.petLocation()) {
                if (this.undefined)
                    view.showUserLocation()
                else
                    view.showLocation(this)
            }
        }
        view.clickedBack {
            if (service.petLocation() != view.selectedLocation())
                service.savePetLocation(view.selectedLocation().copy(date = Date()))
            view.goBack()
        }
    }
}

interface PetLocationView {
    fun visible(action: () -> Unit)
    fun clickedBack(action: () -> Unit)
    fun showUserLocation()
    fun showLocation(location: Location)
    fun selectedLocation(): Location
    fun goBack()
}

interface PetLocationService {
    fun petLocation(): Location
    fun savePetLocation(location: Location)
}
