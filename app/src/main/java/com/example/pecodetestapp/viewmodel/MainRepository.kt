package com.example.pecodetestapp.viewmodel

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.pecodetestapp.fragments.NotificationFragment

class MainRepository (val application: Application){

    private val data = MutableLiveData<ArrayList<Fragment>>()
    private var fragmentList = ArrayList<Fragment>()
    private var sharedPreferences: SharedPreferences =
        application.getSharedPreferences("settings", AppCompatActivity.MODE_PRIVATE)

    private val TAG = MainRepository::class.java.canonicalName

    init {
        getFragments()
    }
    fun getData(): MutableLiveData<ArrayList<Fragment>> {
        return data
    }

    fun addFragment(fragment: Fragment){
        fragmentList.add(fragment)
        data.postValue(fragmentList)
        saveFragments()
    }

    fun removeLastFragment() {
        try{
            fragmentList.removeAt(fragmentList.size - 1)
            data.postValue(fragmentList)
            saveFragments()
        }catch (exception: ArrayIndexOutOfBoundsException){
            exception.message?.let { Log.e(TAG, String.format("The list is already empty: %s", it)) }
        }
    }

    private fun getFragments(){
        val i = sharedPreferences.getInt("fragmentsCount", 0)
        for (k in 0 until i){
            addFragment(NotificationFragment())
            Log.d(TAG, "Restored fragment id: $k")
        }
    }

    fun saveFragments() {
        val editor = sharedPreferences.edit()
        editor.putInt("fragmentsCount", fragmentList.size)
        editor.apply()
        Log.d(TAG, "Fragments were saved")
    }

}
