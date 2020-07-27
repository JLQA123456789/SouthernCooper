package com.example.southerncooper.io.Response

import com.example.southerncooper.model.User

class LoginResponse (val success: Boolean, val user: User, val jwt:String)