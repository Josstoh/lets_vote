package fr.josstoh.letsvote.data.repo.deserializers

interface Deserializer<I, O> {

    @Throws(DeserializerException::class)
    fun deserialize(input: I): O

    class DeserializerException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause)

}
