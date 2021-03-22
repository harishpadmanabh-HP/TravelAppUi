package com.harish.travelappui

import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.harish.travelappui.viewmodels.HomeViewModel
import kotlinx.android.synthetic.main.activity_home_v2.*

class HomeV2 : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_v2)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

    }

    fun handleDrawer(){
        if(drawer_layout.isDrawerOpen(nav_view))
            drawer_layout.closeDrawer(nav_view)
        else
            drawer_layout.openDrawer(nav_view)
    }




}