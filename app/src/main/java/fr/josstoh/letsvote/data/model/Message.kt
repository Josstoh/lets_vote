package fr.josstoh.letsvote.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Message(val from: String = "", val text: String = "", @ServerTimestamp val timestamp: Date? = null)

