package io.github.isidoresong.legacyrefactorpoccontext.point.port

interface PointPort {
    fun grantByPolicy(userId: String, policyCode: String): Long?
    fun revokeByPolicy(userId: String, policyCode: String)
}