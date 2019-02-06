package fr.josstoh.letsvote.data.repo.deserializers

import com.google.firebase.firestore.DocumentSnapshot
import fr.josstoh.letsvote.data.model.Group

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
