package presentation.pets

import android.view.View
import case.ListPetSummariesCase.PetSummary
import com.drodobyte.coreandroid.ui.adapter.RVOAdapter
import com.drodobyte.lostpet.R
import kotlinx.android.synthetic.main.pet_item.*
import util.xLoadPetIcon


class PetsAdapter : RVOAdapter<PetSummary>(R.layout.pet_item) {

    override fun newHolder(view: View) = object : Holder<PetSummary>(view) {
        override fun bind(item: PetSummary, position: Int) {
            pet_name.text = item.name
            pet_image.xLoadPetIcon(item.imageUrl)
            pet_item.setOnClickListener {
                emitClick(item)
            }
        }
    }
}
