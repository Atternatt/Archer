package com.m2f.library.datasource

import arrow.core.Either
import arrow.core.None
import arrow.core.Option
import arrow.core.computations.either
import arrow.core.some
import com.m2f.library.failure.DataNotFound
import com.m2f.library.failure.Failure
import com.m2f.library.failure.QueryNotSupported
import com.m2f.library.query.KeyQuery

class InMemoryDataSource<K, A>(initialValues: Map<K, A> = emptyMap()) : DataSource<Failure, KeyQuery<K, A>, A> {

    private val values: MutableMap<K, A> by lazy { initialValues.toMutableMap() }

    override suspend fun invoke(q: KeyQuery<K, A>): Either<Failure, Option<A>> {
        return when (q) {
            is KeyQuery.Put -> either { q.value.also { values[q.key] = q.value }.some() }
            is KeyQuery.Get -> Either.conditionally(
                test = values.containsKey(q.key),
                ifFalse = { DataNotFound },
                ifTrue = { values[q.key]?.some()!! })
            else -> Either.Right(None).also { values.remove(q.key) }
        }
    }
}