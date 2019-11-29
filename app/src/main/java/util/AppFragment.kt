package util

import androidx.lifecycle.ViewModelProviders
import app.App
import com.drodobyte.coreandroid.ctx.BaseFragment

abstract class AppFragment : BaseFragment() {

    val go: Go by lazy {
        Go(this)
    }

    val app: App by lazy {
        activity!!.application as App
    }

    val petViewModel: PetViewModel by lazy {
        ViewModelProviders.of(activity!!)[PetViewModel::class.java]
    }
}
