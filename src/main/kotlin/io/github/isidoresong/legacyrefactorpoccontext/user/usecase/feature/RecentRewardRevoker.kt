package io.github.isidoresong.legacyrefactorpoccontext.user.usecase.feature

import io.github.isidoresong.legacyrefactorpoccontext.coupon.port.CouponPort
import io.github.isidoresong.legacyrefactorpoccontext.point.port.PointPort
import io.github.isidoresong.legacyrefactorpoccontext.user.usecase.context.SuspendUserContext
import org.springframework.stereotype.Component

@Component
class RecentRewardRevoker(
    private val couponPort: CouponPort,
    private val pointPort: PointPort,
) {
    fun revokeRecentGrants(ctx: SuspendUserContext) {
        ctx.lastCouponGrantLogs.forEach { log ->
            couponPort.revokeByCode(log.userId, log.detail)
        }
        ctx.lastPointGrantLogs.forEach { log ->
            pointPort.revokeByPolicy(log.userId, log.detail)
        }
    }
}