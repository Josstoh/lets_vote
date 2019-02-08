package fr.josstoh.letsvote.viewmodel

import fr.josstoh.letsvote.data.model.Group

data class GroupDisplay(val id: String, var name: String = "",
                        var users: List<String> = arrayListOf(),
                        var messages: Array<String> = arrayOf()) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as GroupDisplay

        if (id != other.id) return false
        if (name != other.name) return false
        if (users != other.users) return false
        if (!messages.contentEquals(other.messages)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + users.hashCode()
        result = 31 * result + messages.contentHashCode()
        return result
    }
}

fun Group.toGroupDisplay(id: String) = GroupDisplay(
    id, this.name, this.users, this.messages
)