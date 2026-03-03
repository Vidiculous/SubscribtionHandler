package com.subhandler.data

import com.subhandler.model.Subscription
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SubscriptionRepository(private val dao: SubscriptionDao) {

    val subscriptions: Flow<List<Subscription>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    suspend fun getById(id: Long): Subscription? =
        dao.getById(id)?.toDomain()

    suspend fun getAllOnce(): List<Subscription> =
        dao.getAllOnce().map { it.toDomain() }

    /** Returns the new row id */
    suspend fun insert(subscription: Subscription): Long =
        dao.insert(subscription.toEntity())

    suspend fun update(subscription: Subscription) =
        dao.update(subscription.toEntity())

    suspend fun delete(id: Long) =
        dao.deleteById(id)
}
