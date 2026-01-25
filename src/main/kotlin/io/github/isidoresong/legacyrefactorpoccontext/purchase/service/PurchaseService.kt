package io.github.isidoresong.legacyrefactorpoccontext.purchase.service

import io.github.isidoresong.legacyrefactorpoccontext.purchase.model.PurchaseHistory
import io.github.isidoresong.legacyrefactorpoccontext.purchase.repository.PurchaseHistoryRepository
import org.springframework.stereotype.Service

@Service
class PurchaseService(
    private val purchaseHistoryRepository: PurchaseHistoryRepository
) {
    fun getLastPurchaseHistory(userId: String): PurchaseHistory? {
        return purchaseHistoryRepository.getLastPurchaseHistory(userId)
    }
}
