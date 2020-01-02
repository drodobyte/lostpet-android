package util

import android.os.Bundle
import android.view.View
import app.App
import com.drodobyte.coreandroid.ctx.BaseFragment
import com.drodobyte.coreandroid.util.Cache
import com.drodobyte.coreandroid.viewmodel.CacheViewModel
import coordinator.Coordinator
import entity.Pet
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf
import service.PetService

abstract class AppFragment : BaseFragment() {

    val app: App by inject()
    val petCache: Cache<Pet> by sharedViewModel<CacheViewModel<Pet>>()
    val petService: PetService by inject()
    val coordinator: Coordinator by inject { parametersOf(this) }
    private lateinit var presenter: AppPresenter

    abstract fun presenter(): AppPresenter

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = presenter()
        onViewCreated(view, savedInstanceState, presenter)
    }

    abstract fun onViewCreated(view: View, saved: Bundle?, presenter: AppPresenter)

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.dispose()
    }
}
