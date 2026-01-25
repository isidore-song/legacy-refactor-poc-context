package io.github.isidoresong.legacyrefactorpoccontext.point.event

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class PointEventListener {
    @EventListener
    fun handlePointGrantEvent(event : PointGrantEvent) {
        println("Consumed Internal Event & Publishing to external system: Point granted to ID - ${event.userId} with PointPolicy - ${event.policyCode}, amount - ${event.pointAmount}")
    }
}