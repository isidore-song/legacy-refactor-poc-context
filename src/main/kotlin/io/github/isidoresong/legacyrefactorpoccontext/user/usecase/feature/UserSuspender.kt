package io.github.isidoresong.legacyrefactorpoccontext.user.usecase.feature

import io.github.isidoresong.legacyrefactorpoccontext.user.model.Status
import io.github.isidoresong.legacyrefactorpoccontext.user.model.User
import io.github.isidoresong.legacyrefactorpoccontext.user.repository.UserRepository
import io.github.isidoresong.legacyrefactorpoccontext.user.usecase.context.SuspendUserContext
import org.springframework.stereotype.Component

@Component
class UserSuspender(
    private val userRepository: UserRepository,
) {
    fun suspend(ctx: SuspendUserContext): User {
        val user = ctx.user
        if (user.status == Status.QUITTER) throw IllegalStateException("User with id '${ctx.userId}' is already a quitter.")
        if (user.status == Status.SUSPENDED) throw IllegalStateException("User with id '${ctx.userId}' is already suspended.")

        return userRepository.save(User(id = user.id, name = user.name, region = user.region, gender = user.gender, status = Status.SUSPENDED))
    }
}