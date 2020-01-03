package presentation.petlocation

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import com.drodobyte.coreandroid.x.backPressObservable
import com.drodobyte.coreandroid.x.moveTo
import com.drodobyte.coreandroid.x.moveToUser
import com.drodobyte.lostpet.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.subjects.PublishSubject
import presentation.petlocation.PetLocationPresenter.Pos
import presentation.petlocation.PetLocationViewState.*
import util.AppFragment
import util.AppPresenter
import util.toLatLon
import util.toPos

class PetLocationFragment : AppFragment(), PetLocationPresenter.View {
    override fun layout() = R.layout.pet_location_fragment
    override fun presenter() =
        PetLocationPresenter(this, PetLocationModel(this::currentPos, petCache), coordinator)

    private val onVisible = PublishSubject.create<Any>()
    private val onPosChanged = PublishSubject.create<Pos>()
    private lateinit var map: GoogleMap
    private var granted: Boolean = false
    private val pin = MarkerOptions()

    override fun onVisible() =
        onVisible

    override fun onClickedBack() =
        requireActivity().backPressObservable()

    override fun onPosChanged() =
        onPosChanged

    override fun render(state: PetLocationViewState) {
        when (state) {
            is Loading -> renderLoading()
            is Ready -> renderPos(state.pos)
            is Updated -> renderPin(state.pos)
        }
    }

    private fun renderLoading() {}

    private fun renderPos(pos: Pos) =
        with(pos) {
            renderPin(pos)
            map.moveTo(x, y, z, true)
        }

    private fun renderPin(pos: Pos) {
        map.clear()
        pin.position(pos.toLatLon())
        map.addMarker(pin)
    }

    override fun onViewCreated(view: View, saved: Bundle?, presenter: AppPresenter) {
        requestLocationPermission()
        (childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment).getMapAsync {
            map = it
            if (granted)
                map.moveToUser(context)
            map.setOnCameraMoveListener {
                onPosChanged.onNext(currentPos())
            }
            onVisible.onNext(Any())
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
        }
    }

    private fun currentPos(): Pos {
        return map.cameraPosition.toPos()
    }
}