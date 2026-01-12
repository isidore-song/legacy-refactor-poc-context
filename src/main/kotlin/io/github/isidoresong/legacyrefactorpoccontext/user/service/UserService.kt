package io.github.isidoresong.legacyrefactorpoccontext.user.service

import io.github.isidoresong.legacyrefactorpoccontext.user.model.Gender
import io.github.isidoresong.legacyrefactorpoccontext.user.model.User
import io.github.isidoresong.legacyrefactorpoccontext.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun getUser(userId: String) : User? = userRepository.findById(userId)
    fun createUser(userId: String, name: String, region: String, gender: Gender) : User {
        if(userRepository.findById(userId) != null) {
            throw IllegalArgumentException("UserId already exists")
        }
        val user = User(id = userId, name = name, region = region, gender = gender)
        userRepository.save(user)
        return user
    }
}