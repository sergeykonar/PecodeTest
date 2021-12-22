package com.example.pecodetestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.pecodetestapp.adapters.NotificationPagerAdapter
import com.example.pecodetestapp.fragments.NotificationFragment
import com.example.pecodetestapp.viewmodel.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.app.NotificationManager
import android.content.Context
import android.os.PersistableBundle
import androidx.core.view.size


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
        mainViewModel.observeFragmentList().observe(this, object: Observer<java.util.ArrayList<Fragment>>{
            override fun onChanged(t: java.util.ArrayList<Fragment>?) {
                if (t != null){
                    Log.e(TAG, "List changed the size")
                    val pagerAdapter = NotificationPagerAdapter(applicationContext, t)
                    viewPager.adapter = pagerAdapter
                    fragments = t
                }
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
        Log.e(TAG, String.format("Notification canceled id: %d", notificationId))
        mainViewModel.removeLastFragment()
    }
}