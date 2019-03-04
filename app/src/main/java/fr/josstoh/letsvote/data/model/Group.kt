package fr.josstoh.letsvote.data.model

import fr.josstoh.letsvote.utils.QueryItemDiffCallback
import fr.josstoh.letsvote.viewmodel.GroupDisplay

data class Group(
    var name: String = "",
    var users: List<String> = arrayListOf(),
    var messages: List<String>? = null)

val groupDiffCallback = object : QueryItemDiffCallback<GroupDisplay>() {}