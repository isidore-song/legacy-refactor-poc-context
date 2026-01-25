package io.github.isidoresong.legacyrefactorpoccontext.point.model

import io.github.isidoresong.legacyrefactorpoccontext.purchase.model.PurchaseHistory
import io.github.isidoresong.legacyrefactorpoccontext.user.model.Gender
import io.github.isidoresong.legacyrefactorpoccontext.user.model.Region
import io.github.isidoresong.legacyrefactorpoccontext.user.model.User

data class PointPolicy(
    val policyCode: String,
    val requiredPurchasePrice: Long,
    val requiredPurchaseAmount: Long,
    val requirePurchaseHistory: Boolean,
    val region: Region?,
    val gender: Gender?,
    val pointAmount: Long,
) {
    fun check(user: User, lastPurchaseHistory: PurchaseHistory?): Boolean {
        if (region != null && user.region != region) return false
        if (gender != null && user.gender != gender) return false

        return when {
            !requirePurchaseHistory -> true
            lastPurchaseHistory == null -> false
            else -> {
                lastPurchaseHistory.amount >= requiredPurchaseAmount &&
                    lastPurchaseHistory.price >= requiredPurchasePrice
            }
        }
    }
}
