package io.github.isidoresong.legacyrefactorpoccontext.coupon.adapter

import io.github.isidoresong.legacyrefactorpoccontext.coupon.port.CouponPort
import io.github.isidoresong.legacyrefactorpoccontext.coupon.service.CouponService
import org.springframework.stereotype.Component

@Component
class LegacyCouponAdapter(
    private val couponService: CouponService
) : CouponPort {

    override fun revokeByCode(userId: String, couponCode: String) {
        couponService.revokeCoupon(userId, couponCode)
    }
}