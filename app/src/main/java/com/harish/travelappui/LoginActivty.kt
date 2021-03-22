package com.harish.travelappui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.harish.travelappui.repository.AuthState
import com.harish.travelappui.viewmodels.UserAuthViewModel
import kotlinx.android.synthetic.main.activity_login_activty.*

class LoginActivty : AppCompatActivity() {
    private lateinit var viewModel: UserAuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_activty)
        initViewModel()
        setupObserver()
        login_btn.setOnClickListener {
            val username = username_til.editText?.text.toString()
            val password = password_til.editText?.text.toString()
            if(username.isEmpty() || password.isEmpty())
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            else{
                loginUser(username, password)
            }

        }
        register_btn.setOnClickListener {
            gotoRegister()
        }

    }

    private fun setupObserver() {
        viewModel.authState.observe(this, Observer {
            when(it){
                AuthState.LOGIN_FAILED->{
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
                AuthState.LOGIN_SUCCESS->{
                    gotoHome()
                }
                AuthState.LOGIN_INIT->{
                    Toast.makeText(this, "Please wait....", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(UserAuthViewModel::class.java)
    }

    fun loginUser(username:String,password:String){
        viewModel.loginUser(username,password)
    }

    fun gotoRegister(){
        startActivity(Intent(this,RegisterActivity::class.java))
    }

    fun gotoHome(){
        startActivity(Intent(this,HomeV2::class.java))
    }
}