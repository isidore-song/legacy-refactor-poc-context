package io.github.isidoresong.legacyrefactorpoccontext.user.usecase.feature

import io.github.isidoresong.legacyrefactorpoccontext.user.event.UserSuspendedEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class UserSuspendedNotifier(
    private val eventPublisher: ApplicationEventPublisher
) {
    fun notify(userId: String) {
        eventPublisher.publishEvent(UserSuspendedEvent(userId))
    }
}