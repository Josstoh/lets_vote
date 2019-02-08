package fr.josstoh.letsvote.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import fr.josstoh.letsvote.common.QueryItem
import fr.josstoh.letsvote.config.AppExecutors
import fr.josstoh.letsvote.data.model.Group
import fr.josstoh.letsvote.data.model.groupDiffCallback
import fr.josstoh.letsvote.databinding.GroupListItemBinding
import fr.josstoh.letsvote.viewmodel.GroupDisplay
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

internal class GroupListAdapter
    : ListAdapter<QueryItem<GroupDisplay>, GroupViewHolder>(asyncDifferConfig) {

    companion object : KoinComponent {
        private val executors by inject<AppExecutors>()
        private val asyncDifferConfig =
            AsyncDifferConfig.Builder<QueryItem<GroupDisplay>>(groupDiffCallback)
                .setBackgroundThreadExecutor(executors.cpuExecutorService)
                .build()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        // Using data binding on the individual views
        val inflater = LayoutInflater.from(parent.context)
        val binding = GroupListItemBinding.inflate(inflater, parent, false)
        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val qItem = getItem(position)
        holder.binding.group = qItem.item

    }

}