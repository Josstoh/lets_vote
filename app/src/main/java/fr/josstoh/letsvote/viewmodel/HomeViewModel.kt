package fr.josstoh.letsvote.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import fr.josstoh.letsvote.common.GroupQueryResults
import fr.josstoh.letsvote.config.AppExecutors
import fr.josstoh.letsvote.data.repo.GroupsRepository
import fr.josstoh.letsvote.livedata.FirestoreQueryLiveData

class HomeViewModel(val repo : GroupsRepository, val executors: AppExecutors, val firestore: FirebaseFirestore) : ViewModel() {

    fun getAllGroups() : LiveData<GroupQueryResults> {
        val groupLiveData = repo.getAllGroupsWhereUserIs(firestore.collection("users").document("joss"))
        return groupLiveData
    }
}