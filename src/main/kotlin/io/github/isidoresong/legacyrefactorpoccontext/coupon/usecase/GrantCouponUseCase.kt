package io.github.isidoresong.legacyrefactorpoccontext.coupon.usecase

import io.github.isidoresong.legacyrefactorpoccontext.coupon.model.CouponGrantResult
import io.github.isidoresong.legacyrefactorpoccontext.coupon.usecase.context.GrantCouponContextFactory
import io.github.isidoresong.legacyrefactorpoccontext.coupon.usecase.feature.CouponIssuer
import io.github.isidoresong.legacyrefactorpoccontext.coupon.usecase.feature.PointCompensator
import org.springframework.stereotype.Service

@Service
class GrantCouponUseCase(
    private val ctxFactory: GrantCouponContextFactory,
    private val couponIssuer: CouponIssuer,
    private val pointCompensator: PointCompensator,
) {
    fun execute(userId: String, couponCode: String): CouponGrantResult {
        val ctx = ctxFactory.create(userId)

        val user = ctx.user
        val coupon = ctx.loadCoupon(couponCode)
        val purchaseHistory = ctx.loadPurchaseHistory()

        if (coupon.check(user, purchaseHistory)) {
            return couponIssuer.issue(userId, coupon, user)
        }

        val policyCode = coupon.compensationPointPolicyCode
        if (policyCode != null) {
            val pointAmount = pointCompensator.compensate(user, purchaseHistory, policyCode)
            if (pointAmount != null) {
                return CouponGrantResult(
                    success = true,
                    user = user,
                    pointPolicyCode = policyCode
                )
            }
        }

        return CouponGrantResult(success = false, user = user)
    }
}