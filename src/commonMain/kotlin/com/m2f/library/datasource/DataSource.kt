package com.m2f.library.datasource

import arrow.core.Either
import arrow.core.Option

interface DataSource<E, Q, A> {
    suspend operator fun invoke(q: Q): Either<E, Option<A>>
}