package io.github.isidoresong.legacyrefactorpoccontext.userAction.service

import io.github.isidoresong.legacyrefactorpoccontext.user.model.ActionType
import io.github.isidoresong.legacyrefactorpoccontext.userAction.repository.UserActionLogRepository
import org.springframework.stereotype.Service

@Service
class UserActionLogService(
    private val userActionLogRepository: UserActionLogRepository
) {
    fun log(action: ActionType, userId: String, detail: String) {
        userActionLogRepository.save(action, userId, detail)
    }
}
