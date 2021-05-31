/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.moviedb

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.view.menu.MenuView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.moviedb.queryDb.QueryDatabase
import com.example.moviedb.queryDb.QueryRepo
import com.example.moviedb.ui.MovieViewModel
import com.example.moviedb.ui.MovieViewModelFactory


/**
 * Main Activity and entry point for the app.
 */
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var navController: NavController
    private lateinit var sharedViewModel:MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get the navigation host fragment from this Activity
        val dao = QueryDatabase.getInstance(context = this).queryDAO
        val repository = QueryRepo(dao)
        val factory = MovieViewModelFactory(repository)
        sharedViewModel = ViewModelProvider(this,factory).get(MovieViewModel::class.java)
        if(savedInstanceState != null)
            sharedViewModel.isNight.value = savedInstanceState.getBoolean("night")
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        // Instantiate the navController using the NavHostFragment
        navController = navHostFragment.navController
        // Make sure actions in the ActionBar get propagated to the NavController
        setupActionBarWithNavController(navController)

    }


    /**
     * Enables back button support. Simply navigates one element up on the stack.
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch_layout -> {
                if(sharedViewModel.isNight.value == true) {
                    sharedViewModel.isNight.value =false
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                    item.setIcon(R.drawable.ic_baseline_nights_stay_24)
                    Log.d("deneme","isNight1: ${sharedViewModel.isNight.value}")
                }
                else if(sharedViewModel.isNight.value == false){
                    sharedViewModel.isNight.value = true
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                    item.setIcon(R.drawable.ic_baseline_wb_sunny_24)
                    Log.d("deneme","isNight2: ${sharedViewModel.isNight.value}")
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

}
