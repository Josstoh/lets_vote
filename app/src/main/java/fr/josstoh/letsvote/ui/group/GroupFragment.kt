package fr.josstoh.letsvote.ui.group


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import fr.josstoh.letsvote.R
import fr.josstoh.letsvote.common.MessageQueryResults
import fr.josstoh.letsvote.ui.home.GroupListAdapter
import fr.josstoh.letsvote.viewmodel.GroupViewModel
import fr.josstoh.letsvote.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.group_fragment.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class GroupFragment : Fragment() {

    val args: GroupFragmentArgs by navArgs()
    private val viewModel by viewModel<GroupViewModel>()
    private val auth by inject<FirebaseAuth>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.group_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageButton>(R.id.send_message).setOnClickListener {
            viewModel.sendMessage(args.groupId, auth.currentUser?.displayName!!, view.findViewById<EditText>(R.id.new_message)?.text.toString())
        }

        view.findViewById<RecyclerView>(R.id.rv_messages).apply {
            layoutManager = LinearLayoutManager(activity)
            val listAdapter = MessageListAdapter()

            val groupsObserver = Observer<MessageQueryResults> {
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

            val groupsLiveData = viewModel.getAllMessages(args.groupId)
            groupsLiveData.observe(this@GroupFragment, groupsObserver)

            adapter = listAdapter

        }
    }
}
