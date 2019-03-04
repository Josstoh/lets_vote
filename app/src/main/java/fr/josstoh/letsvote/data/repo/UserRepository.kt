package fr.josstoh.letsvote.data.repo

import android.util.Log
import androidx.core.content.edit
import androidx.work.ListenableWorker
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import fr.josstoh.letsvote.R
import fr.josstoh.letsvote.data.model.User
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class UserRepository(val firestore: FirebaseFirestore) : KoinComponent {

    private val TAG = "UserRepo"
    private val auth: FirebaseAuth by inject()

    private val usersCollection = firestore.collection("users")

    fun addNewUser(user: User) : Task<DocumentReference> {
        return firestore.runTransaction {
            val userRef = usersCollection.document(user.uid)
            val userDoc = it.get(userRef)
            if(!userDoc.exists()) {
                it.set(userRef, user)
            }
            userRef
        }.addOnSuccessListener { documentReference ->
            Log.d(TAG, "New user added " + documentReference.id)
        }.addOnFailureListener { e ->
            Log.w(TAG, "Error adding new user", e)
        }
    }

    fun addToken(token: String, mUid: String? = auth.currentUser?.uid) : Task<DocumentReference> {
        // todo : stocker l'user en singleton et le modfier à chaque changement
        val uid = mUid ?: throw Exception("User not connected")

        var userRef: DocumentReference? = null
        val task = firestore.collection("users").whereEqualTo("uid", uid).limit(1).get()

        try {
            val documents = Tasks.await(task)
            if(documents.size() != 1)
                throw Exception("No User correspond to this uid")
            else {
                userRef = documents.documents[0].reference
            }
        } catch(e: Exception) {
            Log.w(TAG, "Error getting documents: ", e)
            throw Exception("No User correspond to this uid")
        }

        return firestore.runTransaction {

            val userDoc = it.get(userRef!!)
            if(!userDoc.exists()) {
                Log.d(TAG,"Update Token : User does not exist")
                throw FirebaseFirestoreException("User does not exist", FirebaseFirestoreException.Code.ABORTED)
            }

            var tokens = userDoc["tokens"] as? List<*> ?: throw FirebaseFirestoreException("Error with tokens list", FirebaseFirestoreException.Code.ABORTED)

            if (tokens.contains(token))
                throw FirebaseFirestoreException("Token already present", FirebaseFirestoreException.Code.ABORTED)

            tokens = tokens.plus(token)
            it.update(userRef!!, "tokens", tokens)

            userRef

        }.addOnSuccessListener { documentReference ->
            Log.d(TAG, "Succesfully added new token to user : " + documentReference.id)
        }.addOnFailureListener { e ->
            Log.w(TAG, "Error adding new token to userr", e)
        }
    }
}
