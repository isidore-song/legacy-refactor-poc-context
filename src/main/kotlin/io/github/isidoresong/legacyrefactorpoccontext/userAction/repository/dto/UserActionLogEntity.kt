package io.github.isidoresong.legacyrefactorpoccontext.userAction.repository.dto

import io.github.isidoresong.legacyrefactorpoccontext.user.model.ActionType
import java.time.LocalDateTime

data class UserActionLogEntity(
    val id: Long,
    val userId: String,
    val action: ActionType,
    val detail: String,
    val createdAt: LocalDateTime
)