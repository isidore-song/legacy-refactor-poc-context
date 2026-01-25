package io.github.isidoresong.legacyrefactorpoccontext.point.controller.dto.response

data class PointGrantResponse(
    val userId: String,
    val policyCode: String,
    val grantedPoint: Long,
    val result: Boolean
)