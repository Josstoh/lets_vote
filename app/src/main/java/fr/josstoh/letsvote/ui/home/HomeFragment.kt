package fr.josstoh.letsvote.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import fr.josstoh.letsvote.R
import fr.josstoh.letsvote.viewmodel.GroupDisplayQueryResults
import fr.josstoh.letsvote.viewmodel.HomeViewModel
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

        view.findViewById<RecyclerView>(R.id.rv_messages).apply {
            layoutManager = LinearLayoutManager(activity)
            val listAdapter = GroupListAdapter()

            val groupsObserver = Observer<GroupDisplayQueryResults> {
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

    }


}
