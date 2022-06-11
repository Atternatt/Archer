import com.m2f.library.query.KeyQuery
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*

fun <K, A> Arb.Companion.getKeyQuery(key: Arb<K>): Arb<KeyQuery.Get<K, A>> = key.map { KeyQuery.Get(it) }
fun <K, A> Arb.Companion.putKeyQuery(key: Arb<K>, value: A): Arb<KeyQuery.Put<K, A>> = key.map { KeyQuery.Put(it, value) }
fun <K, A> Arb.Companion.putKeyQuery(key: Arb<K>, value: Arb<A>): Arb<KeyQuery.Put<K, A>> = Arb.bind(key, value) { k, v -> KeyQuery.Put(k, v) }
fun <K, A> Arb.Companion.deleteKeyQuery(key: Arb<K>): Arb<KeyQuery.Delete<K, A>> = key.map { KeyQuery.Delete(it) }

fun <K, A> Arb.Companion.keyQuery(key: Arb<K>, value: Arb<A>): Arb<KeyQuery<K, A>> = arbitraryBuilder {
    when(it.random.nextInt().rem(3)) {
        0 -> Arb.getKeyQuery<K, A>(key).next(it)
        1 -> Arb.putKeyQuery(key, value).next(it)
        else -> Arb.deleteKeyQuery<K, A>(key).next(it)
    }
}