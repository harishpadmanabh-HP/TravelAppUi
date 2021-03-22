package com.harish.travelappui.viewmodels

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import com.harish.travelappui.repository.UserRepository

class UserAuthViewModel(val app: Application):AndroidViewModel(app) {

    val userRepository = UserRepository(app)
    val authState =userRepository.authStatus


    fun createUser(
        name: String,
        username: String,
        email: String,
        password: String,
        lastLocation: Location?,
        uid: Int,
        dpUri: Any
    ) {
        userRepository.registerUser(name, username, email, password,lastLocation,uid,dpUri)
    }

    fun generateUid()=userRepository.generateUid()
    fun loginUser(username: String, password: String) {
        userRepository.loginUser(username,password)

    }

    fun getUser() = userRepository.getUser()

    fun getUserDp(uid:String) = userRepository.getUserDp(uid)


}