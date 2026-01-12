package io.github.isidoresong.legacyrefactorpoccontext.user.event

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class UserEventListener {

    @EventListener
    fun handleUserCreatedEvent(event: UserCreatedEvent) {
        println("Consumed Internal Event & Publishing to external system: User created with ID - ${event.userId}")
    }

    @EventListener
    fun handleUserDeletedEvent(event: UserDeletedEvent) {
        println("Consumed Internal Event & Publishing to external system: User deleted with ID - ${event.userId}")
    }
}