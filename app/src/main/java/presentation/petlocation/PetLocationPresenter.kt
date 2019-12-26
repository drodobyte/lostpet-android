package presentation.petlocation

import entity.Location
import java.util.*

class PetLocationPresenter(view: View, service: Service, coordinator: Coordinator) {
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
            coordinator.onClickedBack()
        }
    }

    interface View {
        fun visible(action: () -> Unit)
        fun clickedBack(action: () -> Unit)
        fun showUserLocation()
        fun showLocation(location: Location)
        fun selectedLocation(): Location
    }

    interface Service {
        fun petLocation(): Location
        fun savePetLocation(location: Location)
    }

    interface Coordinator {
        fun onClickedBack()
    }
}
