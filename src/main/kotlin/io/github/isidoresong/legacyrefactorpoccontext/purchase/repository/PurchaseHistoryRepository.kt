package io.github.isidoresong.legacyrefactorpoccontext.purchase.repository

import io.github.isidoresong.legacyrefactorpoccontext.purchase.model.PurchaseHistory

interface PurchaseHistoryRepository {
    fun getLastPurchaseHistory(userId: String) : PurchaseHistory?
}