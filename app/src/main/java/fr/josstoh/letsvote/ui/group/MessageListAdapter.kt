package fr.josstoh.letsvote.ui.group

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import fr.josstoh.letsvote.common.QueryItem
import fr.josstoh.letsvote.config.AppExecutors
import fr.josstoh.letsvote.data.model.Message
import fr.josstoh.letsvote.databinding.MessageListItemBinding
import fr.josstoh.letsvote.utils.QueryItemDiffCallback
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

internal class MessageListAdapter
    : ListAdapter<QueryItem<Message>, MessageViewHolder>(asyncDifferConfig) {

    companion object : KoinComponent {
        private val executors by inject<AppExecutors>()
        private val asyncDifferConfig =
            AsyncDifferConfig.Builder<QueryItem<Message>>(QueryItemDiffCallback())
                .setBackgroundThreadExecutor(executors.cpuExecutorService)
                .build()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        // Using data binding on the individual views
        val inflater = LayoutInflater.from(parent.context)
        val binding = MessageListItemBinding.inflate(inflater, parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val qItem = getItem(position)
        holder.binding.message = qItem.item

    }

}