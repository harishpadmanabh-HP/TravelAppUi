package com.harish.travelappui.repository

import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore

enum class AuthState{
    REG_SUCCESS,REG_FAILED,REG_INIT,
    LOCATION_RETRIEVED_SUCCES
}

class UserRepository(val context: Context) {

    val db = FirebaseFirestore.getInstance()
    val userMap = HashMap<String, Any>()
    val authStatus = MutableLiveData<AuthState>()

    val preferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)

    fun registerUser(
        name: String,
        username: String,
        email: String,
        password: String,
        lastLocation: Location?,
        uid: Int,
        dpUri: String
    ) {
        authStatus.postValue(AuthState.REG_INIT)

        userMap.put("name", name)
        userMap.put("username", username)
        userMap.put("email", email)
        userMap.put("password", password)
        userMap.put("uid", uid)
        userMap.put("dpUri", dpUri)
        lastLocation?.latitude?.let { userMap.put("latitude", it) }
        lastLocation?.longitude?.let { userMap.put("longitude", it) }



        db.collection("users")
            .document(username).set(userMap).addOnSuccessListener {
              authStatus.postValue(AuthState.REG_SUCCESS)
            }.addOnFailureListener {
              authStatus.postValue(AuthState.REG_FAILED)
            }

    }

    fun generateUid(): Int {
        val uid = (1000..9999).random()
        val editor = preferences.edit()
        editor.putInt("uid",uid)
        editor.commit()
        return uid
    }

    fun hasLoggedin(): HashMap<Boolean,Int> {
        val uid = preferences.getInt("uid",0)
        if(uid!=0){
            return hashMapOf(Pair(true,uid))
        }
        return hashMapOf(Pair(false,uid))
    }

    fun logoutUser(){
        val editor = preferences.edit()
        editor.remove("uid")

    }


}

