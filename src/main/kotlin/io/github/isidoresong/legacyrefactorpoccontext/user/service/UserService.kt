package io.github.isidoresong.legacyrefactorpoccontext.user.service

import io.github.isidoresong.legacyrefactorpoccontext.common.exception.UserAlreadyExistsException
import io.github.isidoresong.legacyrefactorpoccontext.common.exception.UserNotFoundException
import io.github.isidoresong.legacyrefactorpoccontext.coupon.service.CouponService
import io.github.isidoresong.legacyrefactorpoccontext.point.port.PointPort
import io.github.isidoresong.legacyrefactorpoccontext.point.service.PointService
import io.github.isidoresong.legacyrefactorpoccontext.user.event.UserCreatedEvent
import io.github.isidoresong.legacyrefactorpoccontext.user.event.UserDeletedEvent
import io.github.isidoresong.legacyrefactorpoccontext.user.event.UserSuspendedEvent
import io.github.isidoresong.legacyrefactorpoccontext.user.model.ActionType
import io.github.isidoresong.legacyrefactorpoccontext.user.model.Gender
import io.github.isidoresong.legacyrefactorpoccontext.user.model.Region
import io.github.isidoresong.legacyrefactorpoccontext.user.model.User
import io.github.isidoresong.legacyrefactorpoccontext.user.model.Status
import io.github.isidoresong.legacyrefactorpoccontext.user.repository.UserRepository
import io.github.isidoresong.legacyrefactorpoccontext.userAction.service.UserActionLogService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val userActionLogService: UserActionLogService,
    private val pointPort: PointPort,
    private val couponService: CouponService,
) {
    fun getUser(userId: String) : User? = userRepository.findById(userId)
    fun createUser(userId: String, name: String, region: Region, gender: Gender) : User {
        if(userRepository.findById(userId) != null) {
            throw UserAlreadyExistsException("UserId '$userId' already exists")
        }
        val user = User(id = userId, name = name, region = region, gender = gender, status = Status.ACTIVE)
        val savedUser = userRepository.save(user)

        eventPublisher.publishEvent(UserCreatedEvent(savedUser.id))

        return savedUser
    }

    fun deleteUser(userId: String) {
        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException("User with id '$userId' not found.")

        if (user.status == Status.QUITTER) throw IllegalStateException("User with id '$userId' is already a quitter.")

        userRepository.deleteById(userId)

        eventPublisher.publishEvent(UserDeletedEvent(userId))
    }

    fun suspendUser(userId: String) : User {
        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException("User with id '$userId' not found.")

        if (user.status == Status.QUITTER) throw IllegalStateException("User with id '$userId' is already a quitter.")
        if (user.status == Status.SUSPENDED) throw IllegalStateException("User with id '$userId' is already suspended.")

        val savedUser = userRepository.save(User(id = user.id, name = user.name, region = user.region, gender = user.gender, status = Status.SUSPENDED))

        val lastCouponLogs = userActionLogService.getLastActionLogs(ActionType.COUPON_GRANT, userId, 7)
        val lastPointLogs = userActionLogService.getLastActionLogs(ActionType.POINT_GRANT, userId, 7)

        lastCouponLogs.asSequence().forEach {
            couponService.revokeCoupon(it.userId, it.detail)
        }
        lastPointLogs.asSequence().forEach {
            pointPort.revokeByPolicy(it.userId, it.detail)
        }

        eventPublisher.publishEvent(UserSuspendedEvent(userId))

        return savedUser
    }
}