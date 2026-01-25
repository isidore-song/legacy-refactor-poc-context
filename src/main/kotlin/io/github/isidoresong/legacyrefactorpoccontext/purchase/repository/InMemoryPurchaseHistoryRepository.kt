package io.github.isidoresong.legacyrefactorpoccontext.purchase.repository

import io.github.isidoresong.legacyrefactorpoccontext.purchase.model.PurchaseHistory
import io.github.isidoresong.legacyrefactorpoccontext.purchase.repository.dto.PurchaseHistoryEntity
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Repository
class InMemoryPurchaseHistoryRepository : PurchaseHistoryRepository {
    private val purchaseHistoryMap = ConcurrentHashMap<Long, PurchaseHistoryEntity>()
    private val sequence = AtomicLong(0)

    override fun getLastPurchaseHistory(userId: String): PurchaseHistory? {
        return purchaseHistoryMap.values
            .filter { it.userId == userId }
            .maxByOrNull { it.id }
            ?.let { PurchaseHistory(price = it.price, amount = it.amount) }
    }
}