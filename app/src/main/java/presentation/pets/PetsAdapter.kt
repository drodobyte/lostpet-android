package presentation.pets

import android.view.View
import case.ListPetSummariesCase.PetSummary
import com.drodobyte.coreandroid.ui.adapter.RVAdapter
import com.drodobyte.lostpet.R
import kotlinx.android.synthetic.main.pet_item.*
import util.xLoadPetIcon


class PetsAdapter(
    override val listener: (PetSummary) -> Unit
) : RVAdapter<PetSummary>(R.layout.pet_item, listener) {

    override fun newHolder(view: View) = object : Holder<PetSummary>(view) {
        override fun bind(item: PetSummary, position: Int) {
            pet_name.text = item.name
            pet_image.xLoadPetIcon(item.imageUrl)
            pet_item.setOnClickListener {
                listener(item)
            }
        }
    }
}
