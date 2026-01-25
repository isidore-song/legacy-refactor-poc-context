package io.github.isidoresong.legacyrefactorpoccontext.point.repository

import io.github.isidoresong.legacyrefactorpoccontext.point.model.PointPolicy
import io.github.isidoresong.legacyrefactorpoccontext.point.repository.dto.PointPolicyEntity
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class InMemoryPointPolicyRepository : PointPolicyRepository {
    private val pointPolicyMap = ConcurrentHashMap<String, PointPolicyEntity>()

    override fun getActivePointPolicy(policyCode: String): PointPolicy? =
        pointPolicyMap[policyCode]?.takeIf { it.active }?.to()
}