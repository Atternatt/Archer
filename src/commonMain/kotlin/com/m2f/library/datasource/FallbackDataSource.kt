package com.m2f.library.datasource

import arrow.core.Either
import arrow.core.Option
import arrow.core.flatMap
import arrow.core.handleErrorWith
import com.m2f.library.failure.*
import com.m2f.library.operation.CacheSyncOperation
import com.m2f.library.operation.FallbackOperation
import com.m2f.library.operation.MainSyncOperation
import com.m2f.library.query.KeyQuery

class FallbackDataSource<K, A>(
    private val datasource: DataSource<Failure, KeyQuery<K, A>, A>,
    private val fallbackDataSource: DataSource<Failure, KeyQuery<K, A>, A>,
    private val operation: FallbackOperation
) : DataSource<Failure, KeyQuery<K, A>, A> {
    override suspend fun invoke(q: KeyQuery<K, A>): Either<Failure, Option<A>> {
        return when (q) {
            is KeyQuery.Delete, is KeyQuery.Put -> datasource(q).flatMap { fallbackDataSource(q) }
            is KeyQuery.Get -> {
                when (operation) {
                    CacheSyncOperation -> datasource(q).handleErrorWith { failure ->
                        when(failure) {
                            is DataNotFound -> FallbackDataSource(
                                fallbackDataSource,
                                datasource,
                                MainSyncOperation
                            ).invoke(q)
                            else -> Either.Left(failure)
                        }
                    }
                    MainSyncOperation -> datasource(q)
                        .flatMap { it.toEither { DataEmpty } }
                        .flatMap { fallbackDataSource(KeyQuery.put(q.key, it)) }
                        .handleErrorWith { failure ->
                            when (failure) {
                                is ServerError, is NoConnection -> fallbackDataSource(q).mapLeft { failure }
                                else -> Either.Left(failure)
                            }
                        }
                }
            }
        }
    }
}