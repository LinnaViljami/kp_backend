package com.kp_backend.repository
import com.kp_backend.models.LoginRegister
import com.kp_backend.models.User
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction


class UserController {

    fun getAll(): ArrayList<User> {
        val users: ArrayList<User> = arrayListOf()
        transaction {
            Users.selectAll().map {
                users.add(
                    User(
                        id = it[Users.id],
                        password = it[Users.password_hash],
                        email = it[Users.email]
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
                        email = it[Users.email]
                    )
                )
            }
        }
        if(users.isEmpty()){
            transaction {
                Users.insert {
                    it[Users.password_hash] = loginData.password
                    it[Users.email] = loginData.name
                }
                Users.select({ Users.email eq loginData.name }).map {
                    users.add(
                        User(
                            id = it[Users.id],
                            password = it[Users.password_hash],
                            email = it[Users.email]
                        )
                    )
                }
            }
            return users.first()
        }
        else {
            return users.first()
        }
    }
}