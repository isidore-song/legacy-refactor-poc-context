package io.github.isidoresong.legacyrefactorpoccontext.coupon.repository.dto

import io.github.isidoresong.legacyrefactorpoccontext.coupon.model.Coupon
import io.github.isidoresong.legacyrefactorpoccontext.point.repository.dto.PointPolicyEntity
import io.github.isidoresong.legacyrefactorpoccontext.user.model.Gender
import io.github.isidoresong.legacyrefactorpoccontext.user.model.Region
import java.time.LocalDateTime

class CouponEntity(
    val code: String,
    val requiredPurchasePrice: Long,
    val requiredPurchaseAmount: Long,
    val requirePurchaseHistory: Boolean,
    val region: Region?,
    val gender: Gender?,
    val compensationPointPolicyCode: String?,
    val active: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    fun to(): Coupon {
        return Coupon(
            couponCode = this.code,
            requiredPurchasePrice = this.requiredPurchasePrice,
            requiredPurchaseAmount = this.requiredPurchaseAmount,
            requirePurchaseHistory = this.requirePurchaseHistory,
            region = this.region,
            gender = this.gender,
            compensationPointPolicyCode = this.compensationPointPolicyCode
        )
    }
}