package io.github.isidoresong.legacyrefactorpoccontext.user.repository.dto

import io.github.isidoresong.legacyrefactorpoccontext.user.model.Gender
import io.github.isidoresong.legacyrefactorpoccontext.user.model.Region
import io.github.isidoresong.legacyrefactorpoccontext.user.model.Status
import io.github.isidoresong.legacyrefactorpoccontext.user.model.User
import java.time.LocalDateTime

data class UserEntity (
    val id: String,
    val name: String,
    val region: Region,
    val gender: Gender,
    val status: Status,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(user: User): UserEntity {
            val now = LocalDateTime.now()
            return UserEntity(
                id = user.id,
                name = user.name,
                region = user.region,
                gender = user.gender,
                status = user.status,
                createdAt = now,
                updatedAt = now
            )
        }
    }

    fun to(): User{
        return User(
            id = this.id,
            name = this.name,
            region = this.region,
            gender = this.gender,
            status = this.status
        )
    }

    fun quit(): UserEntity {
        return this.copy(
            status = Status.QUITTER,
            updatedAt = LocalDateTime.now()
        )
    }
}