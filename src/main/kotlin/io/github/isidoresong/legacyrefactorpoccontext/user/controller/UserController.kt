package io.github.isidoresong.legacyrefactorpoccontext.user.controller

import io.github.isidoresong.legacyrefactorpoccontext.user.controller.dto.response.UserResponse
import io.github.isidoresong.legacyrefactorpoccontext.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/users")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/{userId:[a-zA-Z0-9._-]+}")
    fun getUser(@PathVariable userId: String) : ResponseEntity<UserResponse> {
        return userService.getUser(userId)
            ?.let { ResponseEntity.ok(UserResponse.of(it)) }
            ?: ResponseEntity.notFound().build()
    }
}