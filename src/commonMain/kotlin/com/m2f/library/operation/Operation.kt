package com.m2f.library.operation

sealed interface Operation

object DefaultOperation: Operation
object MainOperation: Operation
object CacheOperation: Operation
object MainSyncOperation: Operation
object CacheSyncOperation: Operation