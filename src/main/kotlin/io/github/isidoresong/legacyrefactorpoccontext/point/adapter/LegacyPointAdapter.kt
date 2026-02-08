package io.github.isidoresong.legacyrefactorpoccontext.point.adapter

import io.github.isidoresong.legacyrefactorpoccontext.point.port.PointPort
import io.github.isidoresong.legacyrefactorpoccontext.point.service.PointService
import org.springframework.stereotype.Component

@Component
class LegacyPointAdapter(
    private val pointService: PointService
) : PointPort {

    override fun grantByPolicy(userId: String, policyCode: String): Long? {
        return pointService.grantPointByPolicy(userId, policyCode)
    }

    override fun revokeByPolicy(userId: String, policyCode: String) {
        pointService.revokePoint(userId, policyCode)
    }
}