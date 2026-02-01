package io.github.isidoresong.legacyrefactorpoccontext.point.service

import io.github.isidoresong.legacyrefactorpoccontext.common.exception.UserNotFoundException
import io.github.isidoresong.legacyrefactorpoccontext.point.event.PointGrantEvent
import io.github.isidoresong.legacyrefactorpoccontext.point.event.PointRevokeEvent
import io.github.isidoresong.legacyrefactorpoccontext.point.repository.PointPolicyRepository
import io.github.isidoresong.legacyrefactorpoccontext.purchase.model.PurchaseHistory
import io.github.isidoresong.legacyrefactorpoccontext.purchase.service.PurchaseService
import io.github.isidoresong.legacyrefactorpoccontext.user.model.ActionType
import io.github.isidoresong.legacyrefactorpoccontext.user.model.User
import io.github.isidoresong.legacyrefactorpoccontext.user.repository.UserRepository
import io.github.isidoresong.legacyrefactorpoccontext.user.service.UserService
import io.github.isidoresong.legacyrefactorpoccontext.userAction.service.UserActionLogService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class PointService(
    private val userRepository: UserRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val pointPolicyRepository: PointPolicyRepository,
    private val purchaseService: PurchaseService,
    private val userActionLogService: UserActionLogService
) {
    fun grantPointByPolicy(userId: String, policyCode: String): Long? {
        val user = userRepository.findById(userId) ?: throw UserNotFoundException("User with id '$userId' not found.")
        val pointPolicy = pointPolicyRepository.getActivePointPolicy(policyCode) ?: throw IllegalArgumentException("Point policy with code '$policyCode' not found.")
        val purchaseHistory = purchaseService.getLastPurchaseHistory(userId)
        if(pointPolicy.check(user, purchaseHistory)) {
            eventPublisher.publishEvent(PointGrantEvent(userId, policyCode, pointPolicy.pointAmount))
            userActionLogService.log(ActionType.POINT_GRANT, userId, policyCode)
            return pointPolicy.pointAmount
        }
        return null
    }

    fun grantPointByPolicy(user: User, purchaseHistory: PurchaseHistory?, policyCode: String): Long? {
        val pointPolicy = pointPolicyRepository.getActivePointPolicy(policyCode) ?: throw IllegalArgumentException("Point policy with code '$policyCode' not found.")
        if(pointPolicy.check(user, purchaseHistory)) {
            eventPublisher.publishEvent(PointGrantEvent(user.id, pointPolicy.policyCode, pointPolicy.pointAmount))
            userActionLogService.log(ActionType.POINT_GRANT, user.id, pointPolicy.policyCode)
            return pointPolicy.pointAmount
        }
        return null
    }

    fun revokePoint(userId: String, policyCode: String) {
        val user = userRepository.findById(userId) ?: throw UserNotFoundException("User with id '$userId' not found.")
        val pointPolicy = pointPolicyRepository.getPointPolicy(policyCode) ?: throw IllegalArgumentException("Point policy with code '$policyCode' not found.")

        eventPublisher.publishEvent(PointRevokeEvent(userId, policyCode, pointPolicy.pointAmount))
        userActionLogService.log(ActionType.POINT_REVOKE, userId, policyCode)
    }
}
