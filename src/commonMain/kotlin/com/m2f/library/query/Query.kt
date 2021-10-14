package com.m2f.library.query

sealed interface Query<A>

sealed class KeyQuery<K,A>(val key: K): Query<A> {
    class Get<K, A>(key: K): KeyQuery<K, A>(key)
    class Put<K, A>(key: K, val value: A): KeyQuery<K, A>(key)
    class Delete<K, A>(key: K): KeyQuery<K, A>(key)

    companion object {
        fun <K, A> get(key: K): Get<K,A> = Get(key)
        fun <K, A> put(key: K, value: A): Put<K,A> = Put(key, value)
        fun <K, A> delete(key: K): Delete<K,A> = Delete(key)
    }
}
