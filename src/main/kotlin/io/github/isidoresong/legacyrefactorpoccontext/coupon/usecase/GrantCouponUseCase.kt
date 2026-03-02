package io.github.isidoresong.legacyrefactorpoccontext.coupon.usecase

import io.github.isidoresong.legacyrefactorpoccontext.coupon.event.CouponGrantEvent
import io.github.isidoresong.legacyrefactorpoccontext.coupon.model.CouponGrantResult
import io.github.isidoresong.legacyrefactorpoccontext.coupon.repository.CouponRepository
import io.github.isidoresong.legacyrefactorpoccontext.coupon.usecase.context.GrantCouponContext
import io.github.isidoresong.legacyrefactorpoccontext.point.port.PointPort
import io.github.isidoresong.legacyrefactorpoccontext.purchase.service.PurchaseService
import io.github.isidoresong.legacyrefactorpoccontext.user.model.ActionType
import io.github.isidoresong.legacyrefactorpoccontext.user.repository.UserRepository
import io.github.isidoresong.legacyrefactorpoccontext.userAction.service.UserActionLogService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class GrantCouponUseCase(
    private val userRepository: UserRepository,
    private val couponRepository: CouponRepository,
    private val purchaseService: PurchaseService,
    private val pointPort: PointPort,
    private val eventPublisher: ApplicationEventPublisher,
    private val userActionLogService: UserActionLogService,
) {

    fun execute(userId: String, couponCode: String): CouponGrantResult {
        val ctx = GrantCouponContext(
            userId = userId,
            userRepository = userRepository,
            couponRepository = couponRepository,
            purchaseService = purchaseService
        )

        val user = ctx.user
        val coupon = ctx.loadCoupon(couponCode)

        val purchaseHistory = ctx.loadPurchaseHistory()

        if (coupon.check(user, purchaseHistory)) {
            eventPublisher.publishEvent(CouponGrantEvent(userId, couponCode))
            userActionLogService.log(ActionType.COUPON_GRANT, userId, couponCode)
            return CouponGrantResult(success = true, user = user, coupon = coupon)
        }

        val policyCode = coupon.compensationPointPolicyCode
        if (policyCode != null) {
            val pointAmount = pointPort.grantByPolicy(user, purchaseHistory, policyCode)
            if (pointAmount != null) {
                return CouponGrantResult(success = true, user = user, pointPolicyCode = policyCode)
            }
        }

        return CouponGrantResult(success = false, user = user)
    }
}