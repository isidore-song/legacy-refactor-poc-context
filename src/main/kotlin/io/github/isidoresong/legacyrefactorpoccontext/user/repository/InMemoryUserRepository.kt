package io.github.isidoresong.legacyrefactorpoccontext.user.repository

import io.github.isidoresong.legacyrefactorpoccontext.user.model.Gender
import io.github.isidoresong.legacyrefactorpoccontext.user.model.User
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryUserRepository : UserRepository {

    private val userMap = ConcurrentHashMap<String, User>()

    init {
        // 데모용 초기 데이터
        userMap["1"] = User(id = "1", name = "John Doe", region = "Seoul", gender = Gender.MALE)
        userMap["2"] = User(id = "2", name = "Joanne Doe", region = "Busan", gender = Gender.FEMALE)
        userMap["3"] = User(id = "3", name = "Isidore Song", region = "Yongin", gender = Gender.MALE)
        userMap["4"] = User(id = "4", name = "Epica Eldr", region = "Ilsan", gender = Gender.DO_NOT_DECLARE)
    }

    override fun findById(id: String): User? {
        return userMap[id]
    }

}