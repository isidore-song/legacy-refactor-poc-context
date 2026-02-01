package io.github.isidoresong.legacyrefactorpoccontext.coupon.controller.dto.response

data class CouponGrantResponse (
    val userId: String,
    val couponCode: String?,
    val pointPolicy: String?,
    val result: Boolean
)