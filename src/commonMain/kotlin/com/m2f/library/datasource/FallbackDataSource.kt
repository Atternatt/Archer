package com.m2f.library.datasource

import arrow.core.Either
import arrow.core.Option
import arrow.core.handleErrorWith
import com.m2f.library.query.KeyQuery

operator fun <K, E, A> DataSource<E, KeyQuery<K, A>, A>.plus(other: DataSource<E, KeyQuery<K, A>, A>): FallbackDataSource<E, K, A> = FallbackDataSource(this, other)

class FallbackDataSource<E, K, A>(
    private val datasource: DataSource<E, KeyQuery<K, A>, A>,
    private val fallbackDataSource: DataSource<E, KeyQuery<K, A>, A>
) : DataSource<E, KeyQuery<K, A>, A> {
    override suspend fun invoke(q: KeyQuery<K, A>): Either<E, Option<A>> {
        return datasource(q).handleErrorWith { failure ->
            when (q) {
                is KeyQuery.Get -> fallbackDataSource(q)
                is KeyQuery.Delete -> TODO()
                is KeyQuery.Put -> TODO()
            }
        }
    }
}