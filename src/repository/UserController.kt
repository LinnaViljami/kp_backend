package com.kp_backend.repository
import com.kp_backend.Auth.Hasher as MyHasher
import com.kp_backend.models.LoginRegister
import com.kp_backend.models.User
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

import java.util.*
import kotlin.collections.ArrayList
import io.ktor.util.Hash


class UserController {

    private val hasher = MyHasher("SHA-256")

    fun getAll(): ArrayList<User> {
        val users: ArrayList<User> = arrayListOf()
        transaction {
            Users.selectAll().map {
                users.add(
                    User(
                        id = it[Users.id],
                        password = it[Users.password_hash],
                        email = it[Users.email],
                        salt = it[Users.salt]
                    )
                )
            }
        }
        return users
    }

    fun getLoginData(loginData: LoginRegister): User? {

        val users: ArrayList<User> = arrayListOf()
        transaction {
            Users.select({ Users.email eq loginData.name }).map {
                users.add(
                    User(
                        id = it[Users.id],
                        password = it[Users.password_hash],
                        email = it[Users.email],
                        salt = it[Users.salt]
                    )
                )
            }
        }


        if(users.isEmpty()){
            val newSalt = hasher.generateRandomSalt()
            transaction {
                Users.insert {
                    it[Users.password_hash] = hasher.calcHash(loginData.password, newSalt)
                    it[Users.email] = loginData.name
                    it[Users.salt] = newSalt
                }
                println("Generated new hash: " + hasher.calcHash(loginData.password, newSalt))
                Users.select({ Users.email eq loginData.name }).map {
                    users.add(
                        User(
                            id = it[Users.id],
                            password = it[Users.password_hash],
                            email = it[Users.email],
                            salt = it[Users.salt]
                        )
                    )
                }
            }

        }else {
            println("Old hash: " + users.first().password)
            println("Compared hash: " + hasher.calcHash(loginData.password, users.first().salt))
        }
        return users.first()
    }
}