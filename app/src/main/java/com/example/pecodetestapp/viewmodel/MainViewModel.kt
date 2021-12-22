package com.example.pecodetestapp.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MainRepository(application)
    private val data = repository.getData()

    fun observeFragmentList(): MutableLiveData<ArrayList<Fragment>>{
        return data
    }

    fun addFragment(fragment: Fragment){
        repository.addFragment(fragment)
    }

    fun removeLastFragment() {
        repository.removeLastFragment()
    }
}
