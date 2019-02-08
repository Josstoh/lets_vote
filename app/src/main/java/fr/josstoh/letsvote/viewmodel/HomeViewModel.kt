package fr.josstoh.letsvote.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import fr.josstoh.letsvote.common.GroupQueryResults
import fr.josstoh.letsvote.common.QueryItem
import fr.josstoh.letsvote.common.QueryResultsOrException
import fr.josstoh.letsvote.config.AppExecutors
import fr.josstoh.letsvote.data.model.Group
import fr.josstoh.letsvote.data.repo.GroupsRepository

class HomeViewModel(val repo : GroupsRepository, val executors: AppExecutors, val firestore: FirebaseFirestore) : ViewModel() {

    fun getAllGroups() : LiveData<GroupDisplayQueryResults> {
        val groupLiveData = repo.getAllGroupsWhereUserIs(firestore.collection("users").document("joss"))
        return Transformations.map(groupLiveData) { results ->
            val convertedResults = results.data?.map { GroupDisplayQueryItem(it) }
            val exception = results.exception
            GroupDisplayQueryResults(convertedResults, exception)
        }
    }
}

typealias GroupDisplayQueryResults = QueryResultsOrException<GroupDisplay, Exception>

private data class GroupDisplayQueryItem(private val _item: QueryItem<Group>) : QueryItem<GroupDisplay>(_item.item.toGroupDisplay(_item.id), _item.id)