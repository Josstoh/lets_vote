package fr.josstoh.letsvote.common

import fr.josstoh.letsvote.data.model.Group

/**
 * Generic holder for an object that either holds a result of a desired type,
 * or some exception.
 */

data class DataOrException<out T, out E: Exception?>(val data: T?, val exception: E?)

/**
 * An item of data type T that resulted from a query. It adds the notion of
 * a unique id to that item.
 */

interface QueryItem<T> {
    val item: T
    val id: String
}

typealias QueryItemOrException<T> = DataOrException<QueryItem<T>, Exception>


data class GroupQueryItem(private val _item: Group, private val _id: String) : QueryItem<Group> {
    override val item: Group
        get() = _item
    override val id: String
        get() = _id
}

typealias GroupOrException = DataOrException<Group, Exception>

/**
 * The results of a database query (a List of QueryItems), or an Exception.
 */

typealias QueryResultsOrException<T, E> = DataOrException<List<QueryItem<T>>, E>

typealias GroupQueryResults = QueryResultsOrException<Group, Exception>