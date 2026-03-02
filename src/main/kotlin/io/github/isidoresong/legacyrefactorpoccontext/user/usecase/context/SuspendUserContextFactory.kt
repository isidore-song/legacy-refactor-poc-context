package io.github.isidoresong.legacyrefactorpoccontext.user.usecase.context

import io.github.isidoresong.legacyrefactorpoccontext.common.exception.UserNotFoundException
import io.github.isidoresong.legacyrefactorpoccontext.user.repository.UserRepository
import io.github.isidoresong.legacyrefactorpoccontext.userAction.service.UserActionLogService
import org.springframework.stereotype.Component

@Component
class SuspendUserContextFactory(
    private val userRepository: UserRepository,
    private val userActionLogService: UserActionLogService,
) {
    fun create(userId: String): SuspendUserContext {
        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException("User with id '$userId' not found.")
        return SuspendUserContext(userId = userId, user = user, userActionLogService = userActionLogService)
    }
}