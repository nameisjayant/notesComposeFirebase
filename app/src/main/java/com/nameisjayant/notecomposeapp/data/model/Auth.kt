package com.nameisjayant.notecomposeapp.data.model

data class AuthResponse(
    val auth: Auth? = null,
    val key: String? = ""
)

data class Auth(
    val username: String? = "",
    val email: String? = "",
    val password: String? = "",
)