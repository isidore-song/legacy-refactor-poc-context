package io.github.isidoresong.legacyrefactorpoccontext.user.service

import io.github.isidoresong.legacyrefactorpoccontext.common.exception.UserAlreadyExistsException
import io.github.isidoresong.legacyrefactorpoccontext.common.exception.UserNotFoundException
import io.github.isidoresong.legacyrefactorpoccontext.user.event.UserCreatedEvent
import io.github.isidoresong.legacyrefactorpoccontext.user.event.UserDeletedEvent
import io.github.isidoresong.legacyrefactorpoccontext.user.model.Gender
import io.github.isidoresong.legacyrefactorpoccontext.user.model.User
import io.github.isidoresong.legacyrefactorpoccontext.user.model.Status
import io.github.isidoresong.legacyrefactorpoccontext.user.repository.UserRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val eventPublisher: ApplicationEventPublisher,
) {
    fun getUser(userId: String) : User? = userRepository.findById(userId)
    fun createUser(userId: String, name: String, region: String, gender: Gender) : User {
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
}