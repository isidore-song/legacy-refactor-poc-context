package io.github.isidoresong.legacyrefactorpoccontext.coupon.repository

import io.github.isidoresong.legacyrefactorpoccontext.coupon.model.Coupon
import io.github.isidoresong.legacyrefactorpoccontext.coupon.repository.dto.CouponEntity
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryCouponRepository : CouponRepository{
    private val couponMap = ConcurrentHashMap<String, CouponEntity>()

    override fun findByCouponCode(couponCode: String): Coupon? =
        couponMap[couponCode]?.takeIf { it.active }?.to()
}