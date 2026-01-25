package io.github.isidoresong.legacyrefactorpoccontext.point.controller

import io.github.isidoresong.legacyrefactorpoccontext.point.controller.dto.request.PointGrantRequest
import io.github.isidoresong.legacyrefactorpoccontext.point.controller.dto.response.PointGrantResponse
import io.github.isidoresong.legacyrefactorpoccontext.point.service.PointService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/points")
class PointController(
    private val pointService: PointService
) {

    @PostMapping
    fun grantPoint(@RequestBody request: PointGrantRequest): ResponseEntity<PointGrantResponse> {
        val grantedPoint = pointService.grantPointByPolicy(request.userId, request.policyCode)

        return if (grantedPoint != null) {
            ResponseEntity.ok(
                PointGrantResponse(userId = request.userId, policyCode = request.policyCode, grantedPoint = grantedPoint, result = true)
            )
        } else {
            ResponseEntity.ok(
                PointGrantResponse(userId = request.userId, policyCode = request.policyCode, grantedPoint = 0, result = false)
            )
        }
    }
}