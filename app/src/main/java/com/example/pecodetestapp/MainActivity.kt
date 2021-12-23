package com.example.pecodetestapp

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.pecodetestapp.adapters.NotificationPagerAdapter
import com.example.pecodetestapp.fragments.NotificationFragment
import com.example.pecodetestapp.viewmodel.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var viewPager: ViewPager2
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var fabRemove: FloatingActionButton
    private lateinit var fragments: ArrayList<Fragment>

    private val TAG = MainActivity::class.java.canonicalName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        initViews()

        subscribeObservers()
    }

    private fun initViews(){
        viewPager = findViewById(R.id.viewPager)
        fabAdd = findViewById(R.id.fabAdd)
        fabRemove = findViewById(R.id.fabRemove)

        fabAdd.setOnClickListener(fabAddListener)
        fabRemove.setOnClickListener(fabRemoveListener)
    }

    private fun subscribeObservers(){
        mainViewModel.observeFragmentList().observe(this,
            { fragmentList ->
                if (fragmentList != null){
                    Log.e(TAG, "List changed the size")
                    val pagerAdapter = NotificationPagerAdapter(applicationContext, fragmentList)
                    viewPager.adapter = pagerAdapter
                    fragments = fragmentList
                }
            })
    }

    private val fabAddListener = View.OnClickListener {
       mainViewModel.addFragment(NotificationFragment(applicationContext))
    }

    private val fabRemoveListener = View.OnClickListener {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = viewPager.adapter!!.itemCount - 1
        notificationManager.cancel(notificationId)
        Log.e(TAG, String.format("Notification/fragment removed id: %d", notificationId))
        mainViewModel.removeLastFragment()
    }

    @SuppressLint("CommitPrefEdits")
    override fun onStop() {
        super.onStop()
        mainViewModel.saveFragments()
    }
}