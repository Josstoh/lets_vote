package fr.josstoh.letsvote.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import fr.josstoh.letsvote.common.MessageQueryResults
import fr.josstoh.letsvote.config.AppExecutors
import fr.josstoh.letsvote.data.repo.GroupsRepository

class GroupViewModel(val repo : GroupsRepository, val executors: AppExecutors, val firestore: FirebaseFirestore) : ViewModel() {

    fun getAllMessages(groupId: String) : LiveData<MessageQueryResults> {
        val groupLiveData = repo.getMessagesFromGroup(groupId)
        return groupLiveData
    }
}