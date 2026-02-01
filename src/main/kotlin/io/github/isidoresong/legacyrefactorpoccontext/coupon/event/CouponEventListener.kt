package io.github.isidoresong.legacyrefactorpoccontext.coupon.event

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class CouponEventListener {
    @EventListener
    fun handleCouponGrantEvent(event: CouponGrantEvent) {
        println("Consumed Internal Event & Publishing to external system: Coupon granted to ID - ${event.userId} with CouponCode - ${event.couponCode}")
    }
}