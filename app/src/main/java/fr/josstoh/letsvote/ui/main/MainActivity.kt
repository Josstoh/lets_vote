package fr.josstoh.letsvote.ui.main

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavHost
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import fr.josstoh.letsvote.AddTokenWorker
import fr.josstoh.letsvote.R
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val auth by inject<FirebaseAuth>()
    private val tokenPreferences: SharedPreferences by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    override fun onSupportNavigateUp()
            = findNavController(R.id.nav_host).navigateUp()

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authStateListener)
    }

    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        if (auth.currentUser == null) {
            Navigation.findNavController(this, R.id.nav_host).navigate(R.id.action_mainFragment_to_loginFragment)
        } else {
            val addToken = getString(R.string.TOKEN_PREFERENCE_KEY_ADD_TOKEN)
            if(tokenPreferences.getBoolean(addToken, true)) {
                // new token was not registered, we create a work for it
                val addTokenWork = OneTimeWorkRequestBuilder<AddTokenWorker>().build()
                WorkManager.getInstance().enqueueUniqueWork(getString(R.string.TOKEN_PREFERENCE_KEY_ADD_TOKEN), ExistingWorkPolicy.KEEP, addTokenWork)
            }
        }
    }
}
