package com.example.plaindex

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private val recentFiles: MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>().also {
            loadRecentFiles()
        }
    }

    fun getRecentFiles(): LiveData<List<String>> {
        return recentFiles
    }

    private fun loadRecentFiles() {
        // Do an asynchronous operation to fetch users.
    }
}
