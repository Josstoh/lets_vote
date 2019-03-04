package fr.josstoh.letsvote.koin

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import fr.josstoh.letsvote.R
import fr.josstoh.letsvote.config.AppExecutors
import fr.josstoh.letsvote.data.repo.GroupsRepository
import fr.josstoh.letsvote.data.repo.UserRepository
import fr.josstoh.letsvote.viewmodel.GroupViewModel
import fr.josstoh.letsvote.viewmodel.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val firebaseModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
}

val appModule = module {
    single { AppExecutors.instance }

    single(name = "token_preference") { androidContext().getSharedPreferences(androidContext().getString(R.string.TOKEN_PREFERENCE_FILE_KEY), Context.MODE_PRIVATE) }

    single { UserRepository(get()) }
    single { GroupsRepository(get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { GroupViewModel(get(), get(), get()) }
}

val allModules = listOf(appModule, firebaseModule)