package io.github.isidoresong.legacyrefactorpoccontext.user.usecase.context

import io.github.isidoresong.legacyrefactorpoccontext.user.model.ActionType
import io.github.isidoresong.legacyrefactorpoccontext.user.model.User
import io.github.isidoresong.legacyrefactorpoccontext.userAction.service.UserActionLogService

class SuspendUserContext(
    val userId: String,
    val user: User,
    private val userActionLogService: UserActionLogService,
) {
    val lastCouponGrantLogs by lazy {
        userActionLogService.getLastActionLogs(ActionType.COUPON_GRANT, userId, 7)
    }

    val lastPointGrantLogs by lazy {
        userActionLogService.getLastActionLogs(ActionType.POINT_GRANT, userId, 7)
    }
}