package com.harish.travelappui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Gravity.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.harish.travelappui.adapter.CitiesAdapter
import com.harish.travelappui.models.CitiesResponse
import com.harish.travelappui.viewmodels.HomeViewModel
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel:HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupRecyclerShimmer()
        initViewModel()
        setupObservers()
        viewModel.getCities()
        burger_btn.setOnClickListener {
            drawer_layout.openDrawer(drawer,true)
        }
        drawer.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.action_logout->{
                    it.isChecked=true
                    Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show()
                    drawer_layout.closeDrawers()
                }
                R.id.action_update->{
                    Toast.makeText(this, "update", Toast.LENGTH_SHORT).show()
                    drawer_layout.closeDrawers()
                }

            }
            true
        }
    }

    private fun setupRecyclerShimmer() {
        cities_list.shimmerLayoutManager = StaggeredGridLayoutManager(2,RecyclerView.VERTICAL)
        cities_list.shimmerItemCount=8
        cities_list.showShimmer()
    }

    private fun setupObservers() {
        viewModel.apply {
            events.observe(this@HomeActivity, Observer {events->
                Toast.makeText(this@HomeActivity, "$events", Toast.LENGTH_SHORT).show()
            })
            cities.observe(this@HomeActivity, Observer {cityResponse->
                setupRecyclerView(cityResponse)
            })
        }
    }

    private fun setupRecyclerView(cityResponse: CitiesResponse?) {
        cityResponse?.let {
            if(cityResponse.cities.isNullOrEmpty())
            {
                cities_list.hideShimmer()
                Toast.makeText(this, "No CitiesFound", Toast.LENGTH_SHORT).show()

            }

            else{
                cities_list.hideShimmer()
                cities_list.layoutManager = StaggeredGridLayoutManager(2,RecyclerView.VERTICAL)
                cities_list.adapter = CitiesAdapter(cityResponse)
            }

        }

    }

    private fun initViewModel() {
         viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }
}