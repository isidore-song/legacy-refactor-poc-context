package io.github.isidoresong.legacyrefactorpoccontext.coupon.usecase.context

import io.github.isidoresong.legacyrefactorpoccontext.common.exception.UserNotFoundException
import io.github.isidoresong.legacyrefactorpoccontext.coupon.repository.CouponRepository
import io.github.isidoresong.legacyrefactorpoccontext.purchase.service.PurchaseService
import io.github.isidoresong.legacyrefactorpoccontext.user.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class GrantCouponContextFactory(
    private val userRepository: UserRepository,
    private val couponRepository: CouponRepository,
    private val purchaseService: PurchaseService,
) {
    fun create(userId: String): GrantCouponContext {
        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException("User with id '$userId' not found.")
        return GrantCouponContext(
            userId = userId,
            user = user,
            couponRepository = couponRepository,
            purchaseService = purchaseService
        )
    }
}