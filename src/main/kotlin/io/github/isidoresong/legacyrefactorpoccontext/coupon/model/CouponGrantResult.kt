package io.github.isidoresong.legacyrefactorpoccontext.coupon.model

import io.github.isidoresong.legacyrefactorpoccontext.user.model.User

data class CouponGrantResult (
    val success: Boolean,
    val user: User,
    val coupon: Coupon? = null,
    val pointPolicyCode: String? = null
)