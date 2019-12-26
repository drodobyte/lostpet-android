package app

import android.app.Application
import di.DI

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DI.init(this)
    }
}