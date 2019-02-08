package fr.josstoh.letsvote.koin

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import fr.josstoh.letsvote.config.AppExecutors
import fr.josstoh.letsvote.data.repo.GroupsRepository
import fr.josstoh.letsvote.viewmodel.GroupViewModel
import fr.josstoh.letsvote.viewmodel.HomeViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val firebaseModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
}

val appModule = module {
    single { AppExecutors.instance }

    single { GroupsRepository(get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { GroupViewModel(get(), get(), get()) }
}

val allModules = listOf(appModule, firebaseModule)