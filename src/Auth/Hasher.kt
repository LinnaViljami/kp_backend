package com.kp_backend.Auth

import java.util.UUID
import java.security.MessageDigest




class Hasher(val hashingAlgorithm : String ){

    fun hashString(string: String): String {
        val bytes = MessageDigest.getInstance(hashingAlgorithm).digest(string.toByteArray())
        return bytes.joinToString("") {
                "%02x".format(it)
        }
    }


    fun generateRandomSalt() : String {
        return UUID.randomUUID().toString()
    }

    fun calcHash(password : String, salt : String) : String {


        val combinedPassword: String = (password + salt)
        return hashString(combinedPassword)
    }

    fun areEqual(password: String, salt: String, comparedHash: String): Boolean{
        if(calcHash(password, salt) == comparedHash){
            return true
        }
        else {
            return false
        }
    }
}