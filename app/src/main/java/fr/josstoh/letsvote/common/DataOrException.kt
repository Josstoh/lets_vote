package fr.josstoh.letsvote.common

import fr.josstoh.letsvote.data.model.Group
import fr.josstoh.letsvote.data.model.Message

/**
 * Generic holder for an object that either holds a result of a desired type,
 * or some exception.
 */

data class DataOrException<out T, out E: Exception?>(val data: T?, val exception: E?)

/**
 * An item of data type T that resulted from a query. It adds the notion of
 * a unique id to that item.
 */

open class QueryItem<out T>(private val _item: T, private val _id: String) {
     val item: T
         get() = _item
     val id: String
         get() = _id
}

typealias QueryItemOrException<T> = DataOrException<QueryItem<T>, Exception>

typealias GroupOrException = DataOrException<Group, Exception>

/**
 * The results of a database query (a List of QueryItems), or an Exception.
 */

typealias QueryResultsOrException<T, E> = DataOrException<List<QueryItem<T>>, E>

typealias GroupQueryResults = QueryResultsOrException<Group, Exception>

typealias MessageQueryResults = QueryResultsOrException<Message, Exception>