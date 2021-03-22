package com.harish.travelappui.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.harish.travelappui.HomeV2
import com.harish.travelappui.R
import com.harish.travelappui.adapter.CitiesAdapter
import com.harish.travelappui.models.CitiesResponse
import com.harish.travelappui.viewmodels.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment()  {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var root:View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_home, container, false)
        setupRecyclerShimmer()
        setupObservers()
        homeViewModel.getCities()
        root.burger_btn.setOnClickListener {
          val activity=activity as HomeV2
            activity.handleDrawer()
        }
        return root
    }


    private fun setupRecyclerShimmer() {
    root.apply {
        cities_list.shimmerLayoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        cities_list.shimmerItemCount=8
        cities_list.showShimmer()
    }

    }

    private fun setupObservers() {
        homeViewModel.apply {
            events.observe(requireActivity(), Observer {events->
                Toast.makeText(requireActivity(), "$events", Toast.LENGTH_SHORT).show()
            })
            cities.observe(requireActivity(), Observer {cityResponse->
                setupRecyclerView(cityResponse)
            })
        }
    }

    private fun setupRecyclerView(cityResponse: CitiesResponse?) {
        cityResponse?.let {
            if(cityResponse.cities.isNullOrEmpty())
            {
                root.cities_list.hideShimmer()
                Toast.makeText(requireContext(), "No Cities Found", Toast.LENGTH_SHORT).show()

            }

            else{
                root.apply {
                    cities_list.hideShimmer()
                    cities_list.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
                    cities_list.adapter = CitiesAdapter(cityResponse)
                }

            }

        }

    }


}