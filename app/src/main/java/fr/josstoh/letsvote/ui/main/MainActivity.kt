package fr.josstoh.letsvote.ui.main

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import fr.josstoh.letsvote.ui.login.LoginFragment
import fr.josstoh.letsvote.R
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    override fun onSupportNavigateUp()
            = findNavController(R.id.nav_host).navigateUp()
}
