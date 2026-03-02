package io.github.isidoresong.legacyrefactorpoccontext.point.port

import io.github.isidoresong.legacyrefactorpoccontext.purchase.model.PurchaseHistory
import io.github.isidoresong.legacyrefactorpoccontext.user.model.User

interface PointPort {
    fun grantByPolicy(userId: String, policyCode: String): Long?
    fun grantByPolicy(user: User, purchaseHistory: PurchaseHistory?, policyCode: String): Long?
    fun revokeByPolicy(userId: String, policyCode: String)
}