package com.harish.travelappui.repository

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.harish.travelappui.models.User

enum class AuthState{
    REG_SUCCESS,
    REG_FAILED,
    REG_INIT,
    LOCATION_RETRIEVED_SUCCES,
    LOGIN_SUCCESS,
    LOGIN_FAILED,
    LOGIN_INIT
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
        dpUri: Any
    ) {
        authStatus.postValue(AuthState.REG_INIT)

        userMap.put("name", name)
        userMap.put("username", username)
        userMap.put("email", email)
        userMap.put("password", password)
        userMap.put("uid", uid)
        userMap.put("dpUri", dpUri.toString())
        lastLocation?.latitude?.let { userMap.put("latitude", it) }
        lastLocation?.longitude?.let { userMap.put("longitude", it) }
        userMap.put("fcmToken",getFCMToken())

        val user = lastLocation?.let {
            User(
                name, username, password, email, dpUri, it, getFCMToken(), uid
            )
        }




        db.collection("users")
            .document(username).set(userMap).addOnSuccessListener {
                if (user != null) {
                    saveUser(user)
                }
                authStatus.postValue(AuthState.REG_SUCCESS)

            }.addOnFailureListener {
              authStatus.postValue(AuthState.REG_FAILED)
            }

    }

    fun saveUser(user:User){
        val editor = preferences.edit()
        editor.putString("user_data",Gson().toJson(user))
        editor.apply()
    }

    fun getUser():User?{
        val user = Gson().fromJson(preferences.getString("user_data",null),User::class.java)
        return user

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

    fun loginUser(username: String, password: String) {
        authStatus.postValue(AuthState.LOGIN_INIT)

        db.collection("users").document(username)
            .get().addOnSuccessListener {
               Log.e("PASSWORD","${it["password"]}")
                val registerdPasword = it["password"].toString()
                if(registerdPasword.equals(password))
                    authStatus.postValue(AuthState.LOGIN_SUCCESS)
                else
                    authStatus.postValue(AuthState.LOGIN_FAILED)
            }.addOnFailureListener {
                Log.e("LOGIN EXC","$it")
                authStatus.postValue(AuthState.LOGIN_FAILED)

            }



    }


    fun saveFCMToken(token:String){
        val editor=preferences.edit()
        editor.putString("fcm_token",token)
        editor.commit()
    }

    fun getFCMToken():String{
        var token: String = preferences.getString("fcm_token","").toString()
        return token
    }

    fun getUserDp(uid:String):StorageReference = FirebaseStorage.getInstance().reference.child("pics/$uid")


}

