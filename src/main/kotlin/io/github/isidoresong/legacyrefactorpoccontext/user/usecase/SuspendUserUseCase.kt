package io.github.isidoresong.legacyrefactorpoccontext.user.usecase

import io.github.isidoresong.legacyrefactorpoccontext.common.exception.UserNotFoundException
import io.github.isidoresong.legacyrefactorpoccontext.coupon.port.CouponPort
import io.github.isidoresong.legacyrefactorpoccontext.point.port.PointPort
import io.github.isidoresong.legacyrefactorpoccontext.user.event.UserSuspendedEvent
import io.github.isidoresong.legacyrefactorpoccontext.user.model.ActionType
import io.github.isidoresong.legacyrefactorpoccontext.user.model.Status
import io.github.isidoresong.legacyrefactorpoccontext.user.model.User
import io.github.isidoresong.legacyrefactorpoccontext.user.repository.UserRepository
import io.github.isidoresong.legacyrefactorpoccontext.userAction.service.UserActionLogService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class SuspendUserUseCase(
    private val userRepository: UserRepository,
    private val userActionLogService: UserActionLogService,
    private val couponPort: CouponPort,
    private val pointPort: PointPort,
    private val eventPublisher: ApplicationEventPublisher,
) {
    fun execute(userId: String): User {
        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException("User with id '$userId' not found.")

        if (user.status == Status.QUITTER) throw IllegalStateException("User with id '$userId' is already a quitter.")
        if (user.status == Status.SUSPENDED) throw IllegalStateException("User with id '$userId' is already suspended.")

        val savedUser = userRepository.save(User(id = user.id, name = user.name, region = user.region, gender = user.gender, status = Status.SUSPENDED))

        val lastCouponLogs = userActionLogService.getLastActionLogs(ActionType.COUPON_GRANT, userId, 7)
        val lastPointLogs = userActionLogService.getLastActionLogs(ActionType.POINT_GRANT, userId, 7)

        lastCouponLogs.forEach { log ->
            couponPort.revokeByCode(log.userId, log.detail)
        }

        lastPointLogs.forEach { log ->
            pointPort.revokeByPolicy(log.userId, log.detail)
        }

        eventPublisher.publishEvent(UserSuspendedEvent(userId))
        return savedUser
    }
}