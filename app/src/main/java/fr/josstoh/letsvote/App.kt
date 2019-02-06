package fr.josstoh.letsvote

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import fr.josstoh.letsvote.koin.allModules
import org.koin.android.ext.android.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, allModules)
        FirebaseApp.initializeApp(this)
        FirebaseFirestore.setLoggingEnabled(true)
    }
}