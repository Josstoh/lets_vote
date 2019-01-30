package fr.josstoh.letsvote.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import fr.josstoh.letsvote.R
import kotlinx.android.synthetic.main.main_fragment.*


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel

        val user = FirebaseAuth.getInstance().currentUser
        if(user == null)
        {
            NavHostFragment.findNavController(this).navigate(R.id.action_mainFragment_to_loginFragment)
        }
        else
        {
            message.text = "Connected as ${user.displayName}"
            FirebaseApp.initializeApp(activity)
            FirebaseFirestore.setLoggingEnabled(true);
            val db = FirebaseFirestore.getInstance()

            // Create a new user with a first and last name
            val newUser = HashMap<String, Any>()
            newUser["name"] = user.displayName ?: ""
            newUser["email"] = user.email ?: ""

            // Add a new document with a generated ID
            db.collection("users")
                .add(newUser)
                .addOnSuccessListener { documentReference ->
                    Log.d("test", "DocumentSnapshot added with ID: " + documentReference.id)
                }
                .addOnFailureListener { e ->
                    Log.w("test", "Error adding document", e)
                }
        }

    }
}
