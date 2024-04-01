package com.example.githubusersub

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubusersub.data.local.SettingPreferences
import com.example.githubusersub.data.remote.ApiClient
import com.example.githubusersub.utils.MyResult
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class MainViewModel(private val preferences: SettingPreferences): ViewModel() {

    val resultUser = MutableLiveData<MyResult>()

    fun getTheme() = preferences.getThemeSetting().asLiveData()

    fun getUser() {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .githubService
                    .getUserGithub()

                emit(response)
            }.onStart {
                resultUser.value = MyResult.Loading(true)
            }.onCompletion {
                resultUser.value = MyResult.Loading(false)
            }.catch {
                Log.e("Error", it.message.toString())
                it.printStackTrace()
                resultUser.value = MyResult.Error(it)
            }.collect {
                resultUser.value = MyResult.Success(it)
            }
        }
    }

    fun getUser(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .githubService
                    .searchUserGithub(
                        mapOf(
                            "q" to username
                        )
                    )

                emit(response)
            }.onStart {
                resultUser.value = MyResult.Loading(true)
            }.onCompletion {
                resultUser.value = MyResult.Loading(false)
            }.catch {
                Log.e("Error", it.message.toString())
                it.printStackTrace()
                resultUser.value = MyResult.Error(it)
            }.collect {
                resultUser.value = MyResult.Success(it.items)
            }
        }
    }

    class Factory(private val preferences: SettingPreferences) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MainViewModel(preferences) as T
    }
}