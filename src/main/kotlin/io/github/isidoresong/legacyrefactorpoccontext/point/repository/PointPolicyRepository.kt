package io.github.isidoresong.legacyrefactorpoccontext.point.repository

import io.github.isidoresong.legacyrefactorpoccontext.point.model.PointPolicy

interface PointPolicyRepository {
    fun getActivePointPolicy(policyCode: String) : PointPolicy?
}