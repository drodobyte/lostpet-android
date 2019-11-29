package presentation.petgallery

import android.view.View
import com.drodobyte.lostpet.R
import kotlinx.android.synthetic.main.pets_gallery_item.*
import util.AppAdapter
import util.xLoadPetIcon

class PetsGalleryAdapter(override val listener: (String) -> Unit) :
    AppAdapter<String>(R.layout.pets_gallery_item, listener) {

    override fun newHolder(view: View): Holder<String> = object : Holder<String>(view) {
        override fun bind(item: String, position: Int) {
            pet_image.xLoadPetIcon(item)
            pet_image.setOnClickListener {
                listener(item)
            }
        }
    }
}
