package com.m2f.library.operation

sealed interface Operation

object DefaultOperation : Operation
object MainOperation : Operation
object CacheOperation : Operation
sealed interface FallbackOperation : Operation
object MainSyncOperation : FallbackOperation
object CacheSyncOperation : FallbackOperation
