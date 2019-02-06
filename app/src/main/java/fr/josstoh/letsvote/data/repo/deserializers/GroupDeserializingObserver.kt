package fr.josstoh.letsvote.data.repo.deserializers

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import fr.josstoh.letsvote.common.GroupQueryItem
import fr.josstoh.letsvote.common.QueryResultsOrException
import fr.josstoh.letsvote.data.model.Group
import fr.josstoh.letsvote.livedata.DocumentSnapshotsOrException


// TODO Kick off to executor via subclass
// generify better
internal class GroupDeserializingObserver(
    private val liveData: MediatorLiveData<QueryResultsOrException<Group, Exception>>,
    private val deserializer: GroupDocumentSnapshotDeserializer
) : Observer<DocumentSnapshotsOrException> {

    override fun onChanged(results: DocumentSnapshotsOrException?) {
        if (results != null) {
            val (snapshots, exception) = results
            if (snapshots != null) {
                val items = snapshots.map { snapshot ->
                    val group = deserializer.deserialize(snapshot)
                    GroupQueryItem(group, snapshot.id)
                }
                liveData.postValue(QueryResultsOrException(items, null))
            }
            else if (exception != null) {
                liveData.postValue(QueryResultsOrException(null, exception))
            }
        }
    }

}