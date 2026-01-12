package io.github.isidoresong.legacyrefactorpoccontext.user.controller.dto.request

data class UserCreateRequest(
    val userId: String,
    val name: String,
    val region: String,
    val gender: String,
)