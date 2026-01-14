package io.github.isidoresong.legacyrefactorpoccontext.user.model

data class User(
    val id: String,
    val name: String,
    val region: String,
    val gender: Gender,
    val status: Status,
) {
}