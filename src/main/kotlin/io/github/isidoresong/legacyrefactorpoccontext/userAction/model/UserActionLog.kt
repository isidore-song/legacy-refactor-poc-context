package io.github.isidoresong.legacyrefactorpoccontext.userAction.model

import io.github.isidoresong.legacyrefactorpoccontext.user.model.ActionType
import java.time.LocalDateTime

class UserActionLog(
    val id: Long,
    val userId: String,
    val action: ActionType,
    val detail: String,
    val createdAt: LocalDateTime
)