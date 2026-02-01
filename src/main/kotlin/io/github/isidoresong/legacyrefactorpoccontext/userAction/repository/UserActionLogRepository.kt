package io.github.isidoresong.legacyrefactorpoccontext.userAction.repository

import io.github.isidoresong.legacyrefactorpoccontext.user.model.ActionType
import io.github.isidoresong.legacyrefactorpoccontext.userAction.model.UserActionLog

interface UserActionLogRepository {
    fun save(action: ActionType, userId: String, detail: String)
    fun getLastActionLogs(action: ActionType, userId: String, before: Long) : List<UserActionLog>
}