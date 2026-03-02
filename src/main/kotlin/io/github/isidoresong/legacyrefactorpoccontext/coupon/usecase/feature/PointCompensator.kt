package io.github.isidoresong.legacyrefactorpoccontext.coupon.usecase.feature

import io.github.isidoresong.legacyrefactorpoccontext.point.port.PointPort
import io.github.isidoresong.legacyrefactorpoccontext.purchase.model.PurchaseHistory
import io.github.isidoresong.legacyrefactorpoccontext.user.model.User
import org.springframework.stereotype.Component

@Component
class PointCompensator(
    private val pointPort: PointPort,
) {
    fun compensate(user: User, purchaseHistory: PurchaseHistory?, policyCode: String): Long? {
        return pointPort.grantByPolicy(user, purchaseHistory, policyCode)
    }
}