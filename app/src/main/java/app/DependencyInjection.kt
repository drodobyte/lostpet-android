package app

import android.content.Context
import com.drodobyte.coreandroid.viewmodel.CacheViewModel
import com.drodobyte.lostpet.BuildConfig
import coordinator.Coordinator
import coordinator.DefaultCoordinator
import entity.Pet
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import service.AndroidPetService
import service.MockPetService
import util.AppFragment

internal object DependencyInjection {

    fun init(app: App) {
        startKoin {
            androidContext(app)
            modules(
                module {
                    single { androidContext() as App }
                    single { newPetService(get()) }
                    viewModel { CacheViewModel<Pet>() }
                    factory { (fragment: AppFragment) -> DefaultCoordinator(fragment) as Coordinator }
                })
        }
    }

    private fun newPetService(context: Context) =
        if (BuildConfig.BUILD_TYPE == "mock") MockPetService() else AndroidPetService(context)
}
