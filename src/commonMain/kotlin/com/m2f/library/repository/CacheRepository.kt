package com.m2f.library.repository

import arrow.core.Either
import arrow.core.Option
import com.m2f.library.datasource.DataSource
import com.m2f.library.failure.Failure
import com.m2f.library.operation.*
import com.m2f.library.query.KeyQuery

class CacheRepository<K, A>(
    private val mainDataSource: DataSource<Failure, KeyQuery<K, A>, A>,
    private val cacheDataSource: DataSource<Failure, KeyQuery<K, A>, A>
) : Repository<Failure, KeyQuery<K, A>, A> {
    override tailrec suspend fun invoke(o: Operation): suspend (KeyQuery<K, A>) -> Either<Failure, Option<A>> {
        return when (o) {
            is DefaultOperation -> invoke(CacheSyncOperation)
            is CacheOperation -> cacheDataSource::invoke
            is MainOperation -> mainDataSource::invoke
            is CacheSyncOperation -> cacheDataSource.fallback(mainDataSource, o)
            is MainSyncOperation -> mainDataSource.fallback(cacheDataSource, o)
        }
    }
}
