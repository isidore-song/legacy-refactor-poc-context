package io.github.isidoresong.legacyrefactorpoccontext.user.event

data class UserCreatedEvent(val userId: String)

data class UserDeletedEvent(val userId: String)