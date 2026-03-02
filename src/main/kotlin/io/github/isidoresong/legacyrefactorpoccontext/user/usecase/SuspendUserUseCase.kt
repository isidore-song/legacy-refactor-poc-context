package io.github.isidoresong.legacyrefactorpoccontext.user.usecase

import io.github.isidoresong.legacyrefactorpoccontext.user.model.User
import io.github.isidoresong.legacyrefactorpoccontext.user.usecase.context.SuspendUserContextFactory
import io.github.isidoresong.legacyrefactorpoccontext.user.usecase.feature.RecentRewardRevoker
import io.github.isidoresong.legacyrefactorpoccontext.user.usecase.feature.UserSuspendedNotifier
import io.github.isidoresong.legacyrefactorpoccontext.user.usecase.feature.UserSuspender
import org.springframework.stereotype.Service

@Service
class SuspendUserUseCase(
    private val ctxFactory: SuspendUserContextFactory,
    private val suspender: UserSuspender,
    private val revoker: RecentRewardRevoker,
    private val notifier: UserSuspendedNotifier,
) {
    fun execute(userId: String): User {
        val ctx = ctxFactory.create(userId)

        val savedUser = suspender.suspend(ctx)
        revoker.revokeRecentGrants(ctx)
        notifier.notify(userId)

        return savedUser
    }
}