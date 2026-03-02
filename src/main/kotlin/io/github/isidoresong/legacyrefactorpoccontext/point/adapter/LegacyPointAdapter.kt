package io.github.isidoresong.legacyrefactorpoccontext.point.adapter

import io.github.isidoresong.legacyrefactorpoccontext.point.port.PointPort
import io.github.isidoresong.legacyrefactorpoccontext.point.service.PointService
import io.github.isidoresong.legacyrefactorpoccontext.purchase.model.PurchaseHistory
import io.github.isidoresong.legacyrefactorpoccontext.user.model.User
import org.springframework.stereotype.Component

@Component
class LegacyPointAdapter(
    private val pointService: PointService
) : PointPort {

    override fun grantByPolicy(userId: String, policyCode: String): Long? {
        return pointService.grantPointByPolicy(userId, policyCode)
    }

    override fun grantByPolicy(user: User, purchaseHistory: PurchaseHistory?, policyCode: String): Long? {
        return pointService.grantPointByPolicy(user, purchaseHistory, policyCode)
    }

    override fun revokeByPolicy(userId: String, policyCode: String) {
        pointService.revokePoint(userId, policyCode)
    }
}