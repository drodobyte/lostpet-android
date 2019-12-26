package di

import android.content.Context
import app.App
import com.drodobyte.lostpet.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import service.AndroidPetService
import service.MockPetService

object DI {

    fun init(app: App) {
        startKoin {
            androidContext(app)
            modules(serviceModule)
        }
    }

    private val serviceModule = module {
        single { newPetService(get()) }
    }

    private fun newPetService(context: Context) =
        if (BuildConfig.BUILD_TYPE == "mock") MockPetService() else AndroidPetService(context)
}
