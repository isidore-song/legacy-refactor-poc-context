package io.github.isidoresong.legacyrefactorpoccontext.coupon.usecase.context

import io.github.isidoresong.legacyrefactorpoccontext.common.exception.UserNotFoundException
import io.github.isidoresong.legacyrefactorpoccontext.coupon.model.Coupon
import io.github.isidoresong.legacyrefactorpoccontext.coupon.repository.CouponRepository
import io.github.isidoresong.legacyrefactorpoccontext.purchase.model.PurchaseHistory
import io.github.isidoresong.legacyrefactorpoccontext.purchase.service.PurchaseService
import io.github.isidoresong.legacyrefactorpoccontext.user.model.User
import io.github.isidoresong.legacyrefactorpoccontext.user.repository.UserRepository

class GrantCouponContext(
    private val userId: String,
    private val userRepository: UserRepository,
    private val couponRepository: CouponRepository,
    private val purchaseService: PurchaseService,
) {
    val user: User by lazy {
        userRepository.findById(userId)
            ?: throw UserNotFoundException("User with id '$userId' not found.")
    }

    private val couponCache = mutableMapOf<String, Coupon>()
    fun loadCoupon(couponCode: String): Coupon =
        couponCache.getOrPut(couponCode) {
            couponRepository.findByCouponCode(couponCode)
                ?: throw IllegalArgumentException("Coupon with code '$couponCode' not found.")
        }

    private val purchaseHistoryLazy: PurchaseHistory? by lazy {
        purchaseService.getLastPurchaseHistory(userId)
    }
    fun loadPurchaseHistory(): PurchaseHistory? = purchaseHistoryLazy
}