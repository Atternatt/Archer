package com.m2f.library.repository

import arrow.core.Either
import arrow.core.Option
import com.m2f.library.datasource.DataSource
import com.m2f.library.datasource.FallbackDataSource
import com.m2f.library.failure.Failure
import com.m2f.library.operation.DefaultOperation
import com.m2f.library.operation.FallbackOperation
import com.m2f.library.operation.Operation
import com.m2f.library.query.KeyQuery


interface Repository<E, Q, A> {

    suspend operator fun invoke(o: Operation = DefaultOperation): suspend (Q) -> Either<E, Option<A>>
}

fun <K, A> DataSource<Failure, KeyQuery<K, A>, A>.fallback(
    with: DataSource<Failure, KeyQuery<K, A>, A>,
    operation: FallbackOperation
): suspend (KeyQuery<K, A>) -> Either<Failure, Option<A>> =
    FallbackDataSource(this, with, operation)::invoke
