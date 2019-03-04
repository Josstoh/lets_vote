package fr.josstoh.letsvote.viewmodel

import fr.josstoh.letsvote.data.model.Group

data class GroupDisplay(val id: String, var name: String = "",
                        var users: List<String> = arrayListOf(),
                        var messages: List<String>? = null)

fun Group.toGroupDisplay(id: String) = GroupDisplay(
    id, this.name, this.users, this.messages
)