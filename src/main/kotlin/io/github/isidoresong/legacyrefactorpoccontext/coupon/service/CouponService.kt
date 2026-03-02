package io.github.isidoresong.legacyrefactorpoccontext.coupon.service

import io.github.isidoresong.legacyrefactorpoccontext.coupon.event.CouponRevokeEvent
import io.github.isidoresong.legacyrefactorpoccontext.user.model.ActionType
import io.github.isidoresong.legacyrefactorpoccontext.userAction.service.UserActionLogService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class CouponService (
    private val eventPublisher: ApplicationEventPublisher,
    private val userActionLogService: UserActionLogService
) {
    fun revokeCoupon(userId: String, couponCode: String) {
        eventPublisher.publishEvent(CouponRevokeEvent(userId, couponCode))
        userActionLogService.log(ActionType.COUPON_REVOKE, userId, couponCode)
    }
}