package fr.josstoh.letsvote.common

/**
 * Generic holder for an object that either holds a result of a desired type,
 * or some exception.
 */

data class DataOrException<out T, out E: Exception?>(val data: T?, val exception: E?)