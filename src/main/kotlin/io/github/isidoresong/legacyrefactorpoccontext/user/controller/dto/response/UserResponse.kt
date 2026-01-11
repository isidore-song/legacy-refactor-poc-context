package io.github.isidoresong.legacyrefactorpoccontext.user.controller.dto.response

import io.github.isidoresong.legacyrefactorpoccontext.user.model.User

data class UserResponse(
    val userId: String,
    val name: String,
    val region: String,
    val gender: String
) {
    companion object {
        fun of(user: User): UserResponse {
            return UserResponse(
                userId = user.id,
                name = user.name,
                region = user.region,
                gender = user.gender.name
            )
        }
    }
}