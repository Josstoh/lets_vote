package fr.josstoh.letsvote

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import fr.josstoh.letsvote.data.repo.UserRepository
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.lang.Exception

class AddTokenWorker(context : Context, params : WorkerParameters)
    : Worker(context, params), KoinComponent {

    private val userRepo: UserRepository by inject()
    private val auth: FirebaseAuth by inject()
    private val tokenPreferences: SharedPreferences by inject()

    override fun doWork(): Result {
        if(!tokenPreferences.getBoolean(applicationContext.getString(R.string.TOKEN_PREFERENCE_KEY_ADD_TOKEN), false))
            // New token already added
            return Result.failure()

        auth.currentUser?.uid.let { uid ->
            tokenPreferences.getString(applicationContext.getString(R.string.TOKEN_PREFERENCE_KEY_FCM_TOKEN), null)?.let {token ->
                return try {
                    Tasks.await(userRepo.addToken(token, uid))
                    // Put add token preference to false
                    tokenPreferences.edit(commit = true) {
                        putBoolean(applicationContext.getString(R.string.TOKEN_PREFERENCE_KEY_ADD_TOKEN), false)
                    }

                    Result.success()
                } catch(e: Exception) {
                    Result.retry()
                }
            }
        }

        return Result.retry()
    }
}