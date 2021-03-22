package com.harish.travelappui.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.harish.travelappui.GlideApp
import com.harish.travelappui.R
import com.harish.travelappui.models.User
import com.harish.travelappui.viewmodels.HomeViewModel
import com.harish.travelappui.viewmodels.UserAuthViewModel
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {

    private lateinit var viewModel: UserAuthViewModel
    var user: User?=null
    private lateinit var root:View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this).get(UserAuthViewModel::class.java)
         root = inflater.inflate(R.layout.fragment_profile, container, false)
        user = viewModel.getUser()
        renderData(user)
        return root
    }

    private fun renderData(user: User?) {
        user?.let { user->
            root.apply {
                name_til.editText?.setText(user.name)
                uesrname_til.editText?.setText(user.username)
                email_til.editText?.setText(user.email)

                val storageReference = viewModel.getUserDp(user.uid.toString())

                Log.e("DP","$storageReference")

                GlideApp.with(context)
                    .load(viewModel.getUserDp(user.uid.toString()))
                    .error(R.drawable.art)
                    .into(iv_profile_image)



            }
        }

    }
}