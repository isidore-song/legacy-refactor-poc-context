package io.github.isidoresong.legacyrefactorpoccontext.coupon.controller.dto.request

data class CouponGrantRequest(
    val userId: String,
    val couponCode: String,
)