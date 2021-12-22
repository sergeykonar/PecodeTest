package com.example.pecodetestapp.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainRepository (val application: Application){

    private val data = MutableLiveData<ArrayList<Fragment>>()
    private var fragmentList = ArrayList<Fragment>()
    private lateinit var sharedPreferences: SharedPreferences

    private val TAG = MainRepository::class.java.canonicalName

    fun getData(): MutableLiveData<ArrayList<Fragment>> {
        return data
    }

    fun addFragment(fragment: Fragment){
        fragmentList.add(fragment)
        data.postValue(fragmentList)
//        saveFragments()
    }

    fun removeLastFragment() {
        try{
            fragmentList.removeAt(fragmentList.size - 1)
            data.postValue(fragmentList)
        }catch (exception: ArrayIndexOutOfBoundsException){
            exception.message?.let { Log.e(TAG, String.format("The list is already empty: %s", it)) }
        }
    }

}
