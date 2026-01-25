package io.github.isidoresong.legacyrefactorpoccontext.point.repository.dto

import io.github.isidoresong.legacyrefactorpoccontext.point.model.PointPolicy
import io.github.isidoresong.legacyrefactorpoccontext.user.model.Gender
import io.github.isidoresong.legacyrefactorpoccontext.user.model.Region
import java.time.LocalDateTime

class PointPolicyEntity(
    val policyCode: String,
    val requiredPurchasePrice: Long,
    val requiredPurchaseAmount: Long,
    val requirePurchaseHistory: Boolean,
    val region: Region?,
    val gender: Gender?,
    val pointAmount: Long,
    val active: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    fun to(): PointPolicy {
        return PointPolicy(
            policyCode = this.policyCode,
            requiredPurchasePrice = this.requiredPurchasePrice,
            requiredPurchaseAmount = this.requiredPurchaseAmount,
            requirePurchaseHistory = this.requirePurchaseHistory,
            region = this.region,
            gender = this.gender,
            pointAmount = this.pointAmount
        )
    }
}