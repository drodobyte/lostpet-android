package app

import android.content.Context
import com.drodobyte.lostpet.BuildConfig.BUILD_TYPE
import service.AndroidPetService
import service.MockPetService
import service.PetService

object Container {

    lateinit var context: Context

    val petService: PetService by lazy {
        if (BUILD_TYPE == "mock")
            MockPetService()
        else
            AndroidPetService(context)
    }
}