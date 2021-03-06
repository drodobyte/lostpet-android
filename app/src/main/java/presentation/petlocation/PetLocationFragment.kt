package presentation.petlocation

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import com.drodobyte.coreandroid.x.moveTo
import com.drodobyte.coreandroid.x.moveToUser
import com.drodobyte.coreandroid.x.onBackPressed
import com.drodobyte.lostpet.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import coordinator.Coordinator
import entity.Location
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import presentation.petlocation.PetLocationPresenter.Service
import util.AppFragment
import util.toLocation

class PetLocationFragment : AppFragment(), PetLocationPresenter.View, Service {
    override fun layout() = R.layout.pet_location_fragment

    private val coordinator: Coordinator by inject { parametersOf(this) }
    private val visible = PublishSubject.create<Any>()
    private val clickedBack = PublishSubject.create<Any>()
    private lateinit var map: GoogleMap
    private var granted: Boolean = false
    private val marker = MarkerOptions()

    override fun visible(action: () -> Unit) =
        visible.xSubscribe(action)

    override fun clickedBack(action: () -> Unit) =
        clickedBack.xSubscribe(action)

    override fun showUserLocation() {
        requestLocationPermission()
        if (granted)
            map.moveToUser(context)
    }

    override fun showLocation(location: Location) =
        with(location) { map.moveTo(x, y, z, true) }

    override fun selectedLocation() =
        map.cameraPosition.toLocation().copy(date = petLocation().date)

    override fun petLocation() =
        petViewModel.pet.location

    override fun savePetLocation(location: Location) {
        petViewModel.pet = petViewModel.pet.copy(location = location)
    }

    override fun onViewCreated(view: View, saved: Bundle?) {
        PetLocationPresenter(this, this, coordinator)
        (childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment).getMapAsync {
            map = it
            map.setOnCameraIdleListener {
                map.clear()
                marker.position(map.cameraPosition.target)
                map.addMarker(marker)
            }
            visible.onNext(Any())
        }
        requireActivity().onBackPressed {
            clickedBack.onNext(Any())
        }
    }

    private fun requestLocationPermission() =
        if (checkSelfPermission(context!!, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED)
            granted = true
        else
            requestPermissions(activity!!, arrayOf(ACCESS_FINE_LOCATION), 0)

    override fun onRequestPermissionsResult(
        code: Int, permissions: Array<out String>, results: IntArray
    ) {
        if (code == 0 && results.isNotEmpty() && results[0] == PERMISSION_GRANTED) {
            granted = true
            map.moveToUser(context)
        }
    }
}