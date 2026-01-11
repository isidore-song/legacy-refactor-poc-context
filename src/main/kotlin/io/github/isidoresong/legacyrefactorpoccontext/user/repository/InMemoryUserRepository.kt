package io.github.isidoresong.legacyrefactorpoccontext.user.repository

import io.github.isidoresong.legacyrefactorpoccontext.user.model.Gender
import io.github.isidoresong.legacyrefactorpoccontext.user.model.Status
import io.github.isidoresong.legacyrefactorpoccontext.user.model.User
import io.github.isidoresong.legacyrefactorpoccontext.user.repository.dto.UserEntity
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryUserRepository : UserRepository {

    private val userMap = ConcurrentHashMap<String, UserEntity>()

    init {
        // 데모용 초기 데이터
        userMap["1"] =
            UserEntity.from(
                User(
                    id = "1",
                    name = "John Doe",
                    region = "Seoul",
                    gender = Gender.MALE,
                    status = Status.ACTIVE
                )
            )
        userMap["2"] = UserEntity.from(
            User(
                id = "2",
                name = "Joanne Doe",
                region = "Busan",
                gender = Gender.FEMALE,
                status = Status.ACTIVE
            )
        )
        userMap["3"] = UserEntity.from(
            User(
                id = "3",
                name = "Isidore Song",
                region = "Yongin",
                gender = Gender.MALE,
                status = Status.QUITTER
            )
        )
        userMap["4"] = UserEntity.from(
            User(
                id = "4",
                name = "Epica Eldr",
                region = "Ilsan",
                gender = Gender.FEMALE,
                status = Status.ACTIVE
            )
        )
        userMap["5"] = UserEntity.from(
            User(
                id = "5",
                name = "Elfin Doe",
                region = "Seoul",
                gender = Gender.FEMALE,
                status = Status.QUITTER
            )
        )
        userMap["6"] = UserEntity.from(
            User(
                id = "6",
                name = "Vivi Eldr",
                region = "Seoul",
                gender = Gender.FEMALE,
                status = Status.ACTIVE
            )
        )
    }

    override fun findById(id: String): User? {
        return userMap[id]?.to()
    }

    override fun save(user: User): User {
        val existingEntity = userMap[user.id]
        val now = LocalDateTime.now()

        val newEntity = if (existingEntity == null) {
            UserEntity.from(user)
        } else {
            existingEntity.copy(
                name = user.name,
                region = user.region,
                gender = user.gender,
                status = user.status,
                updatedAt = now
            )
        }
        userMap[user.id] = newEntity
        return newEntity.to()
    }

    override fun deleteById(id: String) {
        userMap[id]?.let { user ->
            userMap[id] = user.quit()
        }
    }

}