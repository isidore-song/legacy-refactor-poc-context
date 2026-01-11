package io.github.isidoresong.legacyrefactorpoccontext.user.service

import io.github.isidoresong.legacyrefactorpoccontext.user.model.User
import io.github.isidoresong.legacyrefactorpoccontext.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository
) {
    fun getUser(userId: String) : User? = userRepository.findById(userId)
}