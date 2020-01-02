package util

import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.drodobyte.coreandroid.x.asCalendar
import com.drodobyte.coreandroid.x.asDate
import com.drodobyte.coreandroid.x.fromDate
import com.drodobyte.lostpet.R
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.squareup.picasso.Picasso
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import entity.Location
import presentation.petlocation.PetLocationPresenter.Pos
import java.util.*

fun ImageView.xLoadPetIcon(url: String) =
    Picasso.get()
        .load(if (url.isBlank()) "_undef_" else url)
        .resize(150, 150)
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

fun FragmentManager.showDateDialog(date: Date, onDateSet: (Date) -> Unit) {
    val now = date.asCalendar()
    DatePickerDialog.newInstance(
        { _, year, month, day ->
            date.time = Triple(year, month, day).asDate().time
            onDateSet(date)
        },
        now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
    ).show(this, "picker")
}

fun TextView.onClickRunDateDialog(fragmentManager: FragmentManager) {
    setOnClickListener {
        fragmentManager.showDateDialog(text.asDate()) { date ->
            fromDate(date)
        }
    }
}

fun CameraPosition.toPos() =
    Pos(target.latitude, target.longitude, zoom.toDouble())

fun Pos.toLatLon() =
    LatLng(x, y)

fun Location.toPos() = Pos(x, y, z)
