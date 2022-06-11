package com.m2f.library.repository

import io.kotest.core.spec.style.DescribeSpec

class CacheRepositoryTest : DescribeSpec({

    /**
     * - If we use `MainOperation` it'll use mainDataSource
    - If we use `CacheOperation` Will use cacheDataSource
    - If we choose `MainSyncOperation`
    - If it's a get query -> if fails we wan't to try to use cacheDataSource to retrieve the data
    - if it's a put query -> we'll `put` with both datasources
    - If we choose `CacheSyncOperation`
    - if it's a get query -> if it fails we'll use mainDatasource and will use `put` in the cache to retrieve the data
    - if it's a put query -> we'll `put` with both datasources
     */


})
