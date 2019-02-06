package fr.josstoh.letsvote.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import fr.josstoh.letsvote.data.repo.deserializers.DeserializingDocumentSnapshotObserver
import fr.josstoh.letsvote.common.DataOrException
import fr.josstoh.letsvote.common.GroupQueryResults
import fr.josstoh.letsvote.data.model.Group
import fr.josstoh.letsvote.data.repo.deserializers.GroupDeserializingObserver
import fr.josstoh.letsvote.data.repo.deserializers.GroupDocumentSnapshotDeserializer
import fr.josstoh.letsvote.livedata.FirestoreDocumentLiveData
import fr.josstoh.letsvote.livedata.FirestoreQueryLiveData
import org.koin.standalone.KoinComponent

class GroupsRepository(val firestore: FirebaseFirestore) : KoinComponent {

    private val groupsCollection = firestore.collection("groups")

    fun getAllGroupsWhereUserIs(userRef: DocumentReference) : LiveData<GroupQueryResults>{
        //val query = groupsCollection.whereArrayContains("users", userRef).orderBy("name")
        val query = groupsCollection.orderBy("name")
        val queryLiveData = FirestoreQueryLiveData(query)
        val groupLiveData = MediatorLiveData<GroupQueryResults>()
        val queryObserver = GroupDeserializingObserver(groupLiveData, GroupDocumentSnapshotDeserializer())
        groupLiveData.addSource(queryLiveData, queryObserver)
        return groupLiveData
    }
}