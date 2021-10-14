package com.m2f.library.repository

import arrow.core.Either
import arrow.core.Option
import com.m2f.library.datasource.DataSource
import com.m2f.library.failure.Failure
import com.m2f.library.operation.*
import com.m2f.library.query.KeyQuery

class CacheRepository<K, Q: KeyQuery<K, A>, A>(
    private val mainDataSource: DataSource<Failure, Q, A>,
    private val cacheDataSource: DataSource<Failure, Q, A>
) : Repository<Failure, Q, A> {
    override tailrec suspend fun invoke(o: Operation): suspend (Q) -> Either<Failure, Option<A>> {
        return when (o) {
            DefaultOperation -> invoke(MainOperation)
            CacheOperation -> cacheDataSource::invoke
            MainOperation -> mainDataSource::invoke
            CacheSyncOperation -> {
                { q: KeyQuery<K, A> -> when(q) {
                    is KeyQuery.Delete -> TODO()
                    is KeyQuery.Get -> TODO()
                    is KeyQuery.Put -> TODO()
                } }
            }
            MainSyncOperation -> TODO()
        }
    }

}