package fr.josstoh.letsvote.livedata

import com.google.firebase.firestore.*
import fr.josstoh.letsvote.common.DataOrException
import fr.josstoh.letsvote.config.AppExecutors
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

typealias DocumentSnapshotOrException = DataOrException<DocumentSnapshot?, FirebaseFirestoreException?>

class FirestoreDocumentLiveData(private val ref: DocumentReference)
    : LingeringLiveData<DocumentSnapshotOrException>(), EventListener<DocumentSnapshot>, KoinComponent {

    private val executors by inject<AppExecutors>()

    private var listenerRegistration: ListenerRegistration? = null

    override fun beginLingering() {
        listenerRegistration = ref.addSnapshotListener(executors.cpuExecutorService, this)
    }

    override fun endLingering() {
        listenerRegistration?.remove()
    }

    override fun onEvent(snapshot: DocumentSnapshot?, e: FirebaseFirestoreException?) {
        if (snapshot != null) {
            postValue(DocumentSnapshotOrException(snapshot, null))
        }
        else if (e != null) {
            postValue(DocumentSnapshotOrException(null, e))
        }
    }

}