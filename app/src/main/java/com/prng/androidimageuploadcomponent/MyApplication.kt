package com.prng.androidimageuploadcomponent

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.prng.androidimageuploadcomponent.di.AppModule
import com.prng.androidimageuploadcomponent.di.AppModuleImpl

class MyApplication : MultiDexApplication() {
    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        appModule = AppModuleImpl(applicationContext)
    }
}