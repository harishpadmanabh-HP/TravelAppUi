package com.harish.travelappui.repository

import android.content.Context
import com.harish.travelappui.Utils.Utils
import com.harish.travelappui.models.CitiesResponse
import com.harish.travelappui.network.Apis
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CitiesRepository(val context: Context) {

    val api = Apis()

    fun getCities(onApiCallback: (status: Boolean, message: String?, response: CitiesResponse?) -> Unit){
        api.getCities().enqueue(object :Callback<CitiesResponse>{
            override fun onFailure(call: Call<CitiesResponse>, t: Throwable) {
                if(Utils.hasInternet(context))
                    onApiCallback(false,"No intenret",null)
                else
                    onApiCallback(false,"Something went wrong",null)
            }

            override fun onResponse(
                call: Call<CitiesResponse>,
                response: Response<CitiesResponse>
            ) {
                if(response.isSuccessful)
                    onApiCallback(true,null,response.body())
                else
                    onApiCallback(false,"Internal Server Error",null)


            }
        })
    }

}