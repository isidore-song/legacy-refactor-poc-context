package io.github.isidoresong.legacyrefactorpoccontext.point.service

import io.github.isidoresong.legacyrefactorpoccontext.common.exception.UserNotFoundException
import io.github.isidoresong.legacyrefactorpoccontext.point.event.PointGrantEvent
import io.github.isidoresong.legacyrefactorpoccontext.point.repository.PointPolicyRepository
import io.github.isidoresong.legacyrefactorpoccontext.purchase.service.PurchaseService
import io.github.isidoresong.legacyrefactorpoccontext.user.model.ActionType
import io.github.isidoresong.legacyrefactorpoccontext.user.service.UserService
import io.github.isidoresong.legacyrefactorpoccontext.userAction.service.UserActionLogService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class PointService(
    private val userService: UserService,
    private val eventPublisher: ApplicationEventPublisher,
    private val pointPolicyRepository: PointPolicyRepository,
    private val purchaseService: PurchaseService,
    private val userActionLogService: UserActionLogService
) {
    fun grantPointByPolicy(userId: String, policyCode: String): Long? {
        val user = userService.getUser(userId) ?: throw UserNotFoundException("User with id '$userId' not found.")
        val pointPolicy = pointPolicyRepository.getActivePointPolicy(policyCode) ?: throw IllegalArgumentException("Point policy with code '$policyCode' not found.")
        val purchaseHistory = purchaseService.getLastPurchaseHistory(userId)
        if(pointPolicy.check(user, purchaseHistory)) {
            eventPublisher.publishEvent(PointGrantEvent(userId, policyCode, pointPolicy.pointAmount))
            userActionLogService.log(ActionType.POINT_GRANT, userId, policyCode)
            return pointPolicy.pointAmount
        }
        return null
    }
}
