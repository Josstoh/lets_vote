package fr.josstoh.letsvote.data.repo

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import fr.josstoh.letsvote.config.AppExecutors
import fr.josstoh.letsvote.livedata.FirestoreQueryLiveData
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class GroupsRepository : KoinComponent {
    private val executors by inject<AppExecutors>()
    private val firestore by inject<FirebaseFirestore>()

    private val groupsCollection = firestore.collection("groups")

    fun getAllGroupsWhereUserIs(userRef: DocumentReference) : FirestoreQueryLiveData{
        val query = groupsCollection.whereArrayContains("users", userRef).orderBy("name")
        return FirestoreQueryLiveData(query)
    }
}