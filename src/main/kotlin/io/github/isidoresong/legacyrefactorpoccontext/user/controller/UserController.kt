package io.github.isidoresong.legacyrefactorpoccontext.user.controller

import io.github.isidoresong.legacyrefactorpoccontext.user.controller.dto.request.UserCreateRequest
import io.github.isidoresong.legacyrefactorpoccontext.user.controller.dto.response.UserResponse
import io.github.isidoresong.legacyrefactorpoccontext.user.model.Gender
import io.github.isidoresong.legacyrefactorpoccontext.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

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

    @PostMapping
    fun createUser(@RequestBody userCreateRequest: UserCreateRequest) : ResponseEntity<UserResponse> {
        val gender = try {
            Gender.valueOf(userCreateRequest.gender.uppercase())
        } catch (_: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid gender value. Must be one of ${Gender.entries.map { it.name }}")
        }

        val createdUser = userService.createUser(
            userId = userCreateRequest.userId, name = userCreateRequest.name, region = userCreateRequest.region, gender = gender
        )

        val location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdUser.id)
            .toUri()

        return ResponseEntity.created(location).body(UserResponse.of(createdUser))
    }

    @DeleteMapping("/{userId:[a-zA-Z0-9._-]+}")
    fun deleteUser(@PathVariable userId: String): ResponseEntity<Void> {
        userService.deleteUser(userId)
        return ResponseEntity.noContent().build()
    }
}