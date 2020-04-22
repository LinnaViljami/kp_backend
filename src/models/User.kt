package com.kp_backend.models

import java.util.*

data class User(
    val selected: Boolean = false,
    val id: UUID = UUID.randomUUID(),
    val firstName: String,
    val lastName: String,
    val age: Int
)