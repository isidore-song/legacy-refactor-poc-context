package io.github.isidoresong.legacyrefactorpoccontext.user.service

import io.github.isidoresong.legacyrefactorpoccontext.common.exception.UserAlreadyExistsException
import io.github.isidoresong.legacyrefactorpoccontext.common.exception.UserNotFoundException
import io.github.isidoresong.legacyrefactorpoccontext.coupon.service.CouponService
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
    private val pointService: PointService,
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

        // 이슈 1. 순환참조 발생 해결한다는 명분하에 대규모 수정 발생
        // 이슈 2. Transaction의 지원이 불가능한 구조라면 정합성 및 보상을 어떻게 처리할 것인가? 다른 로직에서 suspendUser를 사용한다면 빠짐없이 보상 처리를 할 수 있는가?
        // 이슈 3. 재사용을 위하는 명분하에 회원 및 쿠폰, 포인트정책의 중복 조회 발생, 경우에 따라 외부 호출을 믿는다는 전재하에 생략하나 일관성 유지가 어려움
        lastCouponLogs.asSequence().forEach {
            couponService.revokeCoupon(it.userId, it.detail)
        }
        lastPointLogs.asSequence().forEach {
            pointService.revokePoint(it.userId, it.detail)
        }

        eventPublisher.publishEvent(UserSuspendedEvent(userId))

        return savedUser
    }
}