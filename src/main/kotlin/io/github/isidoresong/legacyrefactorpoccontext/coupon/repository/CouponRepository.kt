package io.github.isidoresong.legacyrefactorpoccontext.coupon.repository

import io.github.isidoresong.legacyrefactorpoccontext.coupon.model.Coupon

interface CouponRepository {
    fun findByCouponCode(couponCode: String) : Coupon?
}