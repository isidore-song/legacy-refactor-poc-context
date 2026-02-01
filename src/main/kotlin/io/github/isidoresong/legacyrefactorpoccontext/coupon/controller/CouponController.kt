package io.github.isidoresong.legacyrefactorpoccontext.coupon.controller

import io.github.isidoresong.legacyrefactorpoccontext.coupon.controller.dto.request.CouponGrantRequest
import io.github.isidoresong.legacyrefactorpoccontext.coupon.controller.dto.response.CouponGrantResponse
import io.github.isidoresong.legacyrefactorpoccontext.coupon.service.CouponService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/coupons")
class CouponController (
    private val couponService: CouponService
) {

    @PostMapping
    fun grantCoupon(@RequestBody request: CouponGrantRequest) : ResponseEntity<CouponGrantResponse> {
        val couponResult = couponService.grantCoupon(request.userId, request.couponCode)
        if(!couponResult.success) {
            return ResponseEntity.ok(
                CouponGrantResponse(
                    userId = request.userId,
                    couponCode = null,
                    pointPolicy = null,
                    result = false
                )
            )
        }
        return ResponseEntity.ok(
            CouponGrantResponse(
                userId = request.userId,
                couponCode = couponResult.coupon?.couponCode,
                pointPolicy = couponResult.pointPolicyCode,
                result = true
            )
        )
    }
}