package io.github.isidoresong.legacyrefactorpoccontext.coupon.usecase.feature

import io.github.isidoresong.legacyrefactorpoccontext.coupon.event.CouponGrantEvent
import io.github.isidoresong.legacyrefactorpoccontext.coupon.model.Coupon
import io.github.isidoresong.legacyrefactorpoccontext.coupon.model.CouponGrantResult
import io.github.isidoresong.legacyrefactorpoccontext.user.model.ActionType
import io.github.isidoresong.legacyrefactorpoccontext.user.model.User
import io.github.isidoresong.legacyrefactorpoccontext.userAction.service.UserActionLogService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class CouponIssuer(
    private val eventPublisher: ApplicationEventPublisher,
    private val userActionLogService: UserActionLogService,
) {
    fun issue(userId: String, coupon: Coupon, user: User): CouponGrantResult {
        eventPublisher.publishEvent(CouponGrantEvent(userId, coupon.couponCode))
        userActionLogService.log(ActionType.COUPON_GRANT, userId, coupon.couponCode)

        return CouponGrantResult(
            success = true,
            user = user,
            coupon = coupon
        )
    }
}