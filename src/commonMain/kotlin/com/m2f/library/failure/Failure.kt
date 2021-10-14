package com.m2f.library.failure

sealed interface Failure

/** Data can't be found */
object DataNotFound : Failure

/** Data is empty */
object DataEmpty : Failure

/**No connection*/
object NoConnection : Failure

/**Server Error*/
object ServerError : Failure

/** The query is not supported */
object QueryNotSupported : Failure

/** Data is not valid*/
data class InvalidObject(val message: String) : Failure

object UnsupportedOperation : Failure

data class Unknown(val exception: Exception) : Failure