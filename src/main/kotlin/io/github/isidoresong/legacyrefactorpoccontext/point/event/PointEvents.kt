package io.github.isidoresong.legacyrefactorpoccontext.point.event

data class PointGrantEvent(val userId: String, val policyCode: String, val pointAmount: Long)