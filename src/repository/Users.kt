package com.kp_backend.repository

import org.jetbrains.exposed.sql.Table

object Users: Table() {
    val id = integer("id").primaryKey()
    val email = text("email")
    val password_hash = text("password_hash")
}