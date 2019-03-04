package fr.josstoh.letsvote.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import fr.josstoh.letsvote.common.GroupQueryResults
import fr.josstoh.letsvote.common.MessageQueryResults
import fr.josstoh.letsvote.common.QueryResultsOrException
import fr.josstoh.letsvote.data.model.Group
import fr.josstoh.letsvote.data.model.Message
import fr.josstoh.letsvote.data.repo.deserializers.DeserializingObserver
import fr.josstoh.letsvote.data.repo.deserializers.GroupDocumentSnapshotDeserializer
import fr.josstoh.letsvote.data.repo.deserializers.MessageDocumentSnapshotDeserializer
import fr.josstoh.letsvote.livedata.FirestoreQueryLiveData
import org.koin.standalone.KoinComponent

class GroupsRepository(val firestore: FirebaseFirestore) : KoinComponent {

    private val groupsCollection = firestore.collection("groups")
    private val messagesCollection = firestore.collection("groups")

    fun getAllGroupsWhereUserIs(userRef: DocumentReference) : LiveData<GroupQueryResults> {
        //val query = groupsCollection.whereArrayContains("users", userRef).orderBy("name")
        val query = groupsCollection.orderBy("name")
        val queryLiveData = FirestoreQueryLiveData(query)
        val groupLiveData = MediatorLiveData<QueryResultsOrException<Group, Exception>>()
        val queryObserver = DeserializingObserver(groupLiveData, GroupDocumentSnapshotDeserializer())
        groupLiveData.addSource(queryLiveData, queryObserver)
        return groupLiveData
    }

    fun getMessagesFromGroup(groupId: String) : LiveData<MessageQueryResults> {
        val query = groupsCollection.document(groupId).collection("messages").orderBy("timestamp")
        val queryLiveData = FirestoreQueryLiveData(query)
        val messagesLiveData = MediatorLiveData<QueryResultsOrException<Message, Exception>>()
        val queryObserver = DeserializingObserver(messagesLiveData, MessageDocumentSnapshotDeserializer())
        messagesLiveData.addSource(queryLiveData, queryObserver)
        return messagesLiveData
    }

    fun sendMessage(groupId: String, message: Message) : Task<DocumentReference> {
        val newDoc = firestore.collection("groups").document(groupId).collection("messages").document()

        return firestore.runTransaction {
            it.set(newDoc, message)
            newDoc
        }.addOnSuccessListener { documentReference ->
            Log.d("send", "Message sent with id: " + documentReference.id)
        }.addOnFailureListener { e ->
            Log.w("send", "Error adding document", e)
        }
    }
}