package com.m2f.library.repository

import arrow.core.Either
import arrow.core.Option
import com.m2f.library.operation.DefaultOperation
import com.m2f.library.operation.Operation

interface Repository<E, Q, A> {

    suspend operator fun invoke(o: Operation = DefaultOperation): suspend (Q) -> Either<E, Option<A>>
}