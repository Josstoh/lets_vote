package fr.josstoh.letsvote.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import fr.josstoh.letsvote.AddTokenWorker
import fr.josstoh.letsvote.R
import fr.josstoh.letsvote.data.model.User
import fr.josstoh.letsvote.data.repo.UserRepository
import org.koin.android.ext.android.inject
import org.koin.standalone.inject
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LoginFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class LoginFragment : Fragment() {
    private val RC_SIGN_IN: Int = 390

    private val userRepo: UserRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Choose authentication providers
        val providers = Arrays.asList( AuthUI.IdpConfig.GoogleBuilder().build())

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                if(response?.isNewUser == true && user != null) {
                    // Add new user to firestore
                    userRepo.addNewUser(User(user.uid, user.displayName!!, user.email!!))

                    val addTokenWork = OneTimeWorkRequestBuilder<AddTokenWorker>().build()
                    WorkManager.getInstance().enqueueUniqueWork(getString(R.string.TOKEN_PREFERENCE_KEY_ADD_TOKEN), ExistingWorkPolicy.KEEP, addTokenWork)
                }
                Log.d("SIGN-IN", "Connected as ${user?.displayName}")
                NavHostFragment.findNavController(this).navigate(R.id.action_global_groupsfragment)
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Log.e("SIGN-IN", "Issue while authenticating" + response?.error?.localizedMessage)
            }
        }
    }
}
