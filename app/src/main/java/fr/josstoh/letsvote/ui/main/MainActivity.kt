package fr.josstoh.letsvote.ui.main

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import fr.josstoh.letsvote.ui.login.LoginFragment
import fr.josstoh.letsvote.R

class MainActivity : AppCompatActivity(),
    LoginFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    override fun onSupportNavigateUp()
            = findNavController(R.id.nav_host).navigateUp()
}
