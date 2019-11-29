package util

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.drodobyte.lostpet.R
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.squareup.picasso.Picasso
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import entity.Location
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*

fun ImageView.xLoadPetIcon(url: String) =
    Picasso.get()
        .load(if (url.isBlank()) "_undef_" else url)
        .resize(140, 140)
        .centerCrop()
        .error(R.drawable.ic_alert_error)
        .placeholder(R.drawable.ic_downloading)
        .into(this)

fun ImageView.xLoadPet(url: String) =
    Picasso.get()
        .load(if (url.isBlank()) "_undef_" else url)
        .error(R.drawable.ic_alert_error)
        .placeholder(R.drawable.ic_downloading)
        .into(this)

fun Date.xFormatted(): String = DATE_FORMAT.format(this)

fun Date.xShowDialog(
    fragmentManager: FragmentManager?, onDateSet: (Date) -> Unit
) {
    val now = asCalendar()
    DatePickerDialog.newInstance(
        { _, year, month, day ->
            val date = Triple(year, month, day).asDate()
            time = date.time
            onDateSet(date)
        },
        now.get(YEAR), now.get(MONTH), now.get(DAY_OF_MONTH)
    ).show(fragmentManager!!, "picker")
}

fun TextView.xDate(date: Date) {
    text = date.xFormatted()
}

fun View.xShow() {
    visibility = View.VISIBLE
}

fun View.xHide() {
    visibility = View.INVISIBLE
}

fun View.xGone() {
    visibility = View.GONE
}

fun View.xShow(show: Boolean) {
    if (show) xShow() else xHide()
}

fun FragmentActivity.onBackPressed(action: () -> Unit) {
    onBackPressedDispatcher.addCallback(
        this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                action()
            }
        }
    )
}

fun CameraPosition.toLocation() = let {
    Location(Date(), it.target.latitude, it.target.longitude, it.zoom.toDouble())
}

fun GoogleMap.showUser(show: Boolean) {
    isMyLocationEnabled = show
    uiSettings.isMyLocationButtonEnabled = show
}

fun GoogleMap.moveTo(location: Location, showUser: Boolean = false) = moveCamera(with(location) {
    showUser(showUser)
    CameraUpdateFactory.newLatLngZoom(LatLng(x, y), z.toFloat())
})

fun GoogleMap.moveToUser(context: Context?) {
    showUser(true)
    LocationServices.getFusedLocationProviderClient(context!!).lastLocation
        .addOnSuccessListener { loc ->
            moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(loc.latitude, loc.longitude), 16f
                )
            )
        }
}

private val DATE_FORMAT = SimpleDateFormat("yyyy/MMM/dd", Locale.getDefault())