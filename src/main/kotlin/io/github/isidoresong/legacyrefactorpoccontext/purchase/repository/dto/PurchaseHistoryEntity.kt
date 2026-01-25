package io.github.isidoresong.legacyrefactorpoccontext.purchase.repository.dto

data class PurchaseHistoryEntity (
    val id: Long,
    val userId: String,
    val price: Long,
    val amount: Long,
)