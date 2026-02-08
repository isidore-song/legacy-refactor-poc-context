package io.github.isidoresong.legacyrefactorpoccontext.coupon.service

import io.github.isidoresong.legacyrefactorpoccontext.common.exception.UserNotFoundException
import io.github.isidoresong.legacyrefactorpoccontext.coupon.event.CouponGrantEvent
import io.github.isidoresong.legacyrefactorpoccontext.coupon.event.CouponRevokeEvent
import io.github.isidoresong.legacyrefactorpoccontext.coupon.model.CouponGrantResult
import io.github.isidoresong.legacyrefactorpoccontext.coupon.repository.CouponRepository
import io.github.isidoresong.legacyrefactorpoccontext.point.port.PointPort
import io.github.isidoresong.legacyrefactorpoccontext.point.service.PointService
import io.github.isidoresong.legacyrefactorpoccontext.purchase.service.PurchaseService
import io.github.isidoresong.legacyrefactorpoccontext.user.model.ActionType
import io.github.isidoresong.legacyrefactorpoccontext.user.repository.UserRepository
import io.github.isidoresong.legacyrefactorpoccontext.user.service.UserService
import io.github.isidoresong.legacyrefactorpoccontext.userAction.service.UserActionLogService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class CouponService (
    private val userRepository: UserRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val couponRepository: CouponRepository,
    private val pointPort: PointPort,
    private val purchaseService: PurchaseService,
    private val userActionLogService: UserActionLogService
) {
    fun grantCoupon(userId: String, couponCode: String) : CouponGrantResult {
        val user = userRepository.findById(userId) ?: throw UserNotFoundException("User with id '$userId' not found.")
        val coupon = couponRepository.findByCouponCode(couponCode) ?: throw IllegalArgumentException("Coupon with code '$couponCode' not found.")
        val purchaseHistory = purchaseService.getLastPurchaseHistory(userId)
        val canApplyCoupon = coupon.check(user, purchaseHistory)
        if(canApplyCoupon) {
            eventPublisher.publishEvent(CouponGrantEvent(userId, couponCode))
            userActionLogService.log(ActionType.COUPON_GRANT, userId, couponCode)
            return CouponGrantResult(
                success = true,
                user = user,
                coupon = coupon
            )
        }
        if(coupon.compensationPointPolicyCode != null){
            val pointAmount = pointPort.grantByPolicy(userId, coupon.compensationPointPolicyCode)

            if(pointAmount != null) {
                return CouponGrantResult(
                    success = true,
                    user = user,
                    pointPolicyCode = coupon.compensationPointPolicyCode
                )
            }
        }
        return CouponGrantResult(
            success = false,
            user = user
        )
    }

    fun revokeCoupon(userId: String, couponCode: String) {
        eventPublisher.publishEvent(CouponRevokeEvent(userId, couponCode))
        userActionLogService.log(ActionType.COUPON_REVOKE, userId, couponCode)
    }
}