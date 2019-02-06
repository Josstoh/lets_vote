package fr.josstoh.letsvote.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import fr.josstoh.letsvote.R
import fr.josstoh.letsvote.common.GroupQueryResults
import fr.josstoh.letsvote.ui.main.MainViewModel
import fr.josstoh.letsvote.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel by viewModel<HomeViewModel>()
    private val auth by inject<FirebaseAuth>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.rv_groups).apply {
            layoutManager = LinearLayoutManager(activity)
            val listAdapter = GroupListAdapter()

            val groupsObserver = Observer<GroupQueryResults> {
                if (it != null) {
                    if (it.data != null) {
                        listAdapter.submitList(it.data)
                    }
                    else if (it.exception != null) {
                        Log.e("homefrag", "Error getting stock history", it.exception)
                        TODO("Handle the error")
                    }
                }
            }

            val groupsLiveData = viewModel.getAllGroups()
            groupsLiveData.observe(this@HomeFragment, groupsObserver)

            adapter = listAdapter

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser


        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("fcm", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                //val msg = getString(R.string.msg_token_fmt, token)
                Log.d("fcm", token)
                Toast.makeText(activity, token, Toast.LENGTH_SHORT).show()
            })

/*        // Create a new user with a first and last name
        val newUser = HashMap<String, Any>()
        newUser["name"] = user?.displayName ?: ""
        newUser["email"] = user?.email ?: ""

        // Add a new document with a generated ID
        db.collection("users")
            .add(newUser)
            .addOnSuccessListener { documentReference ->
                Log.d("test", "DocumentSnapshot added with ID: " + documentReference.id)
            }
            .addOnFailureListener { e ->
                Log.w("test", "Error adding document", e)
            }
        }*/

    }

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
            NavHostFragment.findNavController(this).navigate(R.id.action_mainFragment_to_loginFragment)
        }
    }
}
