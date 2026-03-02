package io.github.isidoresong.legacyrefactorpoccontext.user.usecase.context

import io.github.isidoresong.legacyrefactorpoccontext.common.exception.UserNotFoundException
import io.github.isidoresong.legacyrefactorpoccontext.user.model.ActionType
import io.github.isidoresong.legacyrefactorpoccontext.user.model.User
import io.github.isidoresong.legacyrefactorpoccontext.user.repository.UserRepository
import io.github.isidoresong.legacyrefactorpoccontext.userAction.service.UserActionLogService

class SuspendUserContext(
    private val userId: String,
    private val userRepository: UserRepository,
    private val userActionLogService: UserActionLogService,
) {
    val user: User by lazy {
        userRepository.findById(userId) ?: throw UserNotFoundException("User with id '$userId' not found.")
    }

    val lastCouponGrantLogs by lazy {
        userActionLogService.getLastActionLogs(ActionType.COUPON_GRANT, userId, 7)
    }

    val lastPointGrantLogs by lazy {
        userActionLogService.getLastActionLogs(ActionType.POINT_GRANT, userId, 7)
    }
}