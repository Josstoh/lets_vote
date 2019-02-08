package fr.josstoh.letsvote.ui.home

import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import fr.josstoh.letsvote.databinding.GroupListItemBinding

internal class GroupViewHolder(val binding: GroupListItemBinding)
    : RecyclerView.ViewHolder(binding.root) {

    val groupId
        get() = binding.group?.id

    init {
        binding.root.setOnClickListener {
            val directions = HomeFragmentDirections.openGroupFromHomeFragment(groupId!!)
            Navigation.findNavController(binding.root).navigate(directions)
        }
    }
}