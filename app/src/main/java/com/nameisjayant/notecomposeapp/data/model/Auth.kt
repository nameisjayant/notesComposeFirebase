package com.nameisjayant.notecomposeapp.data.model

data class AuthResponse(
    val auth: Auth? = null,
    val key: String? = null
)

data class Auth(
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val gender: Int? = null,
    val dob: Long? = null,
    val mobileNumber: String? = null
)