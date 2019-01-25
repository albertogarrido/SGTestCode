package net.albertogarrido.studydrivetest

import android.app.Application

import com.jakewharton.threetenabp.AndroidThreeTen

class SDApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}
