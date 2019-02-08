package fr.josstoh.letsvote.data.repo.deserializers

import com.google.firebase.firestore.DocumentSnapshot
import fr.josstoh.letsvote.common.QueryItem
import fr.josstoh.letsvote.data.model.Group
import fr.josstoh.letsvote.data.model.Message

internal interface DocumentSnapshotDeserializer<T> : Deserializer<DocumentSnapshot, T>

internal class GroupDocumentSnapshotDeserializer : DocumentSnapshotDeserializer<Group> {
    override fun deserialize(input: DocumentSnapshot): Group {
        val group = input.toObject<Group>(Group::class.java)
        if (group != null) {
            return group
        }
        else {
            throw Deserializer.DeserializerException("DocumentSnapshot.toObject() returned null")
        }
    }
}

internal class MessageDocumentSnapshotDeserializer : DocumentSnapshotDeserializer<Message> {
    override fun deserialize(input: DocumentSnapshot): Message {
        val message = input.toObject<Message>(Message::class.java)
        if (message != null) {
            return message
        }
        else {
            throw Deserializer.DeserializerException("DocumentSnapshot.toObject() returned null")
        }
    }
}
