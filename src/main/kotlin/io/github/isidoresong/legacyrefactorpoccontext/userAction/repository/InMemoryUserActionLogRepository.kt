package io.github.isidoresong.legacyrefactorpoccontext.userAction.repository

import io.github.isidoresong.legacyrefactorpoccontext.user.model.ActionType
import io.github.isidoresong.legacyrefactorpoccontext.userAction.model.UserActionLog
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

    override fun getLastActionLogs(
        action: ActionType,
        userId: String,
        before: Long
    ): List<UserActionLog> {
        val now = LocalDateTime.now()
        val threshold = now.minusDays(before).toLocalDate().atStartOfDay()

        return userActionMap.values.asSequence()
            .filter { it.action == action }
            .filter { it.userId == userId }
            .filter { !it.createdAt.isBefore(threshold) } // createdAt >= threshold
            .sortedByDescending { it.createdAt }
            .map {
                UserActionLog(
                    id = it.id,
                    userId = it.userId,
                    action = it.action,
                    detail = it.detail,
                    createdAt = it.createdAt
                )
            }
            .toList()
    }
}