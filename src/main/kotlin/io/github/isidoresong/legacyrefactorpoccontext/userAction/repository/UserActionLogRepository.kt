package io.github.isidoresong.legacyrefactorpoccontext.userAction.repository

import io.github.isidoresong.legacyrefactorpoccontext.user.model.ActionType

interface UserActionLogRepository {
    fun save(action: ActionType, userId: String, detail: String)
}