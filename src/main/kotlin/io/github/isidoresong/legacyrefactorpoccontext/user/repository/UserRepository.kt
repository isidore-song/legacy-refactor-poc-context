package io.github.isidoresong.legacyrefactorpoccontext.user.repository

import io.github.isidoresong.legacyrefactorpoccontext.user.model.User

interface UserRepository {
    fun findById(id: String): User?
}