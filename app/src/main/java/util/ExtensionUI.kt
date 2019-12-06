package util

import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import com.drodobyte.coreandroid.x.asCalendar
import com.drodobyte.coreandroid.x.asDate
import com.drodobyte.lostpet.R
import com.google.android.gms.maps.model.CameraPosition
import com.squareup.picasso.Picasso
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import entity.Location
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
        now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
    ).show(fragmentManager!!, "picker")
}

fun CameraPosition.toLocation() = let {
    Location(Date(), it.target.latitude, it.target.longitude, it.zoom.toDouble())
}
