package di

import android.content.Context
import app.App
import com.drodobyte.lostpet.BuildConfig
import coordinator.Coordinator
import coordinator.DefaultCoordinator
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import service.AndroidPetService
import service.MockPetService
import util.AppFragment

object DI {

    fun init(app: App) {
        startKoin {
            androidContext(app)
            modules(listOf(serviceModule, coordinatorModule))
        }
    }

    private val serviceModule = module {
        single { newPetService(get()) }
    }

    private val coordinatorModule = module {
        factory { (fragment: AppFragment) -> DefaultCoordinator(fragment) as Coordinator }
    }

    private fun newPetService(context: Context) =
        if (BuildConfig.BUILD_TYPE == "mock") MockPetService() else AndroidPetService(context)
}
