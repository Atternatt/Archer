package com.m2f.library.datasource

import arrow.core.Either
import arrow.core.None
import arrow.core.some
import com.m2f.library.failure.DataNotFound
import com.m2f.library.query.KeyQuery
import deleteKeyQuery
import getKeyQuery
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll
import io.kotest.property.forAll
import keyQuery
import putKeyQuery

class InMemoryDataSourceTest : FunSpec() {

    init {

        test("InMemoryDataSource returns Either Left or Right") {
            val dataSource: InMemoryDataSource<String, Int> = InMemoryDataSource()
            val getKeys: Arb<KeyQuery<String, Int>> = Arb.keyQuery(Arb.string(), Arb.int())

            forAll(getKeys) { query ->
                dataSource(query).run {
                    this is Either.Right ||
                    this is Either.Left
                }
            }
        }

        test("Adding an objects returns the added object") {
            val dataSource: InMemoryDataSource<String, Int> = InMemoryDataSource()
            val queries = Arb.putKeyQuery(Arb.string(), Arb.int())

            checkAll(queries) { putQuery->
                dataSource(putQuery) shouldBe Either.Right(putQuery.value.some())
            }
        }


        test("The output of the DataSource is the same using Put and Get queries") {
            val dataSource: InMemoryDataSource<String, Int> = InMemoryDataSource()
            val queries = Arb.bind(Arb.string(), Arb.int()) { k, v -> KeyQuery.Get<String, Int>(k) to KeyQuery.Put(k, v)}

            checkAll(queries) { (getQuery, putQuery) ->
                dataSource(putQuery) shouldBe dataSource(getQuery)
            }
        }

        test("Getting an unexisting value returns Left with DataNotFound Failure") {
            val dataSource: InMemoryDataSource<String, Int> = InMemoryDataSource()
            val queries = Arb.getKeyQuery<String, Int>(Arb.string())

            checkAll(queries) { getQuery ->
                    dataSource(getQuery) shouldBe Either.Left(DataNotFound)
            }
        }

        test("Deleting a value always return Right") {
            val dataSource: InMemoryDataSource<String, Int> = InMemoryDataSource()
            val queries = Arb.deleteKeyQuery<String, Int>(Arb.string())

            forAll(queries) { query ->
                dataSource(query) is Either.Right
            }
        }

        test("Deleting a value returns Right with None value") {
            val dataSource: InMemoryDataSource<String, Int> = InMemoryDataSource()
            val queries = Arb.deleteKeyQuery<String, Int>(Arb.string())

            checkAll(queries) { query ->
                dataSource(query) shouldBe  Either.Right(None)
            }
        }

        test("Using Delete query removes an existing value") {

            val dataSource: InMemoryDataSource<String, Int> = InMemoryDataSource()
            val queries = Arb.bind(Arb.string(), Arb.int()) { k, v -> Triple(KeyQuery.Get<String, Int>(k), KeyQuery.Put(k, v), KeyQuery.Delete<String, Int>(k))}

            checkAll(queries) { (get, put, delete) ->
                dataSource(put)
                dataSource(delete)
                dataSource(get) shouldBe Either.Left(DataNotFound)
            }

        }
    }

}