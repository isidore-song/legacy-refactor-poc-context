package io.github.isidoresong.legacyrefactorpoccontext.coupon.port

interface CouponPort {
    fun revokeByCode(userId: String, couponCode: String)
}