package fr.josstoh.letsvote.data.model

import fr.josstoh.letsvote.utils.QueryItemDiffCallback
import fr.josstoh.letsvote.viewmodel.GroupDisplay

data class Group(
    var name: String = "",
    var users: List<String> = arrayListOf(),
    var messages: Array<String> = arrayOf()
) : Any() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Group

        if (name != other.name) return false
        if (users != other.users) return false
        if (!messages.contentEquals(other.messages)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + users.hashCode()
        result = 31 * result + messages.contentHashCode()
        return result
    }
}

val groupDiffCallback = object : QueryItemDiffCallback<GroupDisplay>() {}