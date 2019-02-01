package fr.josstoh.letsvote.livedata

import com.google.firebase.firestore.*
import fr.josstoh.letsvote.common.DataOrException
import fr.josstoh.letsvote.config.AppExecutors
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

typealias DocumentSnapshotsOrException = DataOrException<List<DocumentSnapshot>?, FirebaseFirestoreException?>

class FirestoreQueryLiveData(private val query: Query)
    : LingeringLiveData<DocumentSnapshotsOrException>(), EventListener<QuerySnapshot>, KoinComponent {

    private val executors by inject<AppExecutors>()

    private var listenerRegistration: ListenerRegistration? = null

    override fun beginLingering() {
        listenerRegistration = query.addSnapshotListener(
            executors.cpuExecutorService,
            MetadataChanges.INCLUDE,
            this)
    }

    override fun endLingering() {
        listenerRegistration?.remove()
    }

    override fun onEvent(snapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {
        val documents = snapshot?.documents
        postValue(DocumentSnapshotsOrException(documents, e))
    }

}