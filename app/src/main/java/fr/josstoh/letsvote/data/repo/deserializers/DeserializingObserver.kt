package fr.josstoh.letsvote.data.repo.deserializers

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import fr.josstoh.letsvote.common.DataOrException
import fr.josstoh.letsvote.common.QueryItem
import fr.josstoh.letsvote.common.QueryResultsOrException
import fr.josstoh.letsvote.data.model.Group
import fr.josstoh.letsvote.data.model.Message
import fr.josstoh.letsvote.livedata.DocumentSnapshotsOrException

internal class DeserializingObserver<T : Any>(
    private val liveData: MediatorLiveData<DataOrException<List<QueryItem<T>>, Exception>>,
    private val deserializer: DocumentSnapshotDeserializer<T>
) : Observer<DocumentSnapshotsOrException> {

    override fun onChanged(results: DocumentSnapshotsOrException?) {
        if (results != null) {
            val (snapshots, exception) = results
            if (snapshots != null) {
                val items = snapshots.map { snapshot ->
                    val item = deserializer.deserialize(snapshot)

                    when(item) {
                        is Group -> QueryItem(item, snapshot.id)
                        is Message -> QueryItem(item, snapshot.id)
                        else -> throw NotImplementedError("Desirializing not implemetned for class ${item::class}")
                    }
                }
                @Suppress("UNCHECKED_CAST")
                liveData.postValue(QueryResultsOrException<T, Exception>(items as List<QueryItem<T>>?, exception))
            }
            else if (exception != null) {
                liveData.postValue(QueryResultsOrException<T, Exception>(null, exception))
            }
        }
    }

}