package presentation.pets

import android.view.View
import case.ListPetSummariesCase.PetSummary
import com.drodobyte.lostpet.R
import kotlinx.android.synthetic.main.pet_item.view.*
import util.AppAdapter
import util.xLoadPetIcon


class PetsAdapter(
    override val listener: (PetSummary) -> Unit
) : AppAdapter<PetSummary>(R.layout.pet_item, listener) {

    override fun newHolder(view: View) = object : Holder<PetSummary>(view) {
        override fun bind(item: PetSummary, position: Int) {
            with(itemView) {
                pet_name.text = item.name
                pet_image.xLoadPetIcon(item.imageUrl)
                pet_item.setOnClickListener {
                    listener(item)
                }
            }
        }
    }
}
