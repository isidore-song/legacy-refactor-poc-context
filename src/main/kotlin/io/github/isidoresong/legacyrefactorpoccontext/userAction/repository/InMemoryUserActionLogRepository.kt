package io.github.isidoresong.legacyrefactorpoccontext.userAction.repository

import io.github.isidoresong.legacyrefactorpoccontext.user.model.ActionType
import io.github.isidoresong.legacyrefactorpoccontext.userAction.repository.dto.UserActionLogEntity
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Repository
class InMemoryUserActionLogRepository : UserActionLogRepository {
    private val userActionMap = ConcurrentHashMap<Long, UserActionLogEntity>()
    private val sequence = AtomicLong(0)

    override fun save(
        action: ActionType,
        userId: String,
        detail: String
    ) {
        val newId = sequence.incrementAndGet()
        val newAction = UserActionLogEntity(
            id = newId, userId = userId, action = action, detail = detail, createdAt = LocalDateTime.now()
        )
        userActionMap[newId] = newAction
    }
}