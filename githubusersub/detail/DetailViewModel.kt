package com.example.githubusersub.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.githubusersub.data.local.DbModule
import com.example.githubusersub.data.local.FavoriteUser
import com.example.githubusersub.data.remote.ApiClient
import com.example.githubusersub.utils.MyResult
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


@Suppress("UNCHECKED_CAST")
class DetailViewModel(private val db: DbModule) : ViewModel() {
    val resultDetailUser = MutableLiveData<MyResult>()
    val resultFollowersUser = MutableLiveData<MyResult>()
    val resultFollowingUser = MutableLiveData<MyResult>()
    val resultSuksesFavorite = MutableLiveData<Boolean>()
    val resultDeleteFavorite = MutableLiveData<Boolean>()
    val isFavorite = MutableLiveData(false)

    fun setFavorite(item: FavoriteUser?) {
        viewModelScope.launch {
            item?.let {
                if (isFavorite.value == true) {
                    db.userDao.delete(item)
                    resultDeleteFavorite.value = true
                } else {
                    db.userDao.insert(item)
                    resultSuksesFavorite.value = false
                }
            }
            isFavorite.value = !(isFavorite.value ?: false)
        }
    }

    fun findFavorite(id: Int) {
        viewModelScope.launch {
            val user = db.userDao.findById(id)
            if (user > 0) {
                isFavorite.value = true
            }
        }
    }

    fun getDetailUser(username: String) {
        viewModelScope.launch{
            flow {
                val response = ApiClient
                    .githubService
                    .getDetailUser(username)

                emit(response)
            }.onStart {
                resultDetailUser.value = MyResult.Loading(true)
            }.onCompletion {
                resultDetailUser.value = MyResult.Loading(false)
            }.catch {
                Log.e("Error", it.message.toString())
                it.printStackTrace()
                resultDetailUser.value = MyResult.Error(it)
            }.collect {
                resultDetailUser.value = MyResult.Success(it)
            }
        }
    }

    fun getFollowers(username: String){
        viewModelScope.launch{
            flow {
                val response = ApiClient
                    .githubService
                    .getFollowersUser(username)

                emit(response)
            }.onStart {
                resultFollowersUser.value = MyResult.Loading(true)
            }.onCompletion {
                resultFollowersUser.value = MyResult.Loading(false)
            }.catch {
                Log.e("Error", it.message.toString())
                it.printStackTrace()
                resultFollowersUser.value = MyResult.Error(it)
            }.collect {
                resultFollowersUser.value = MyResult.Success(it)
            }
        }
    }

    fun getFollowing(username: String){
        viewModelScope.launch{
            flow {
                val response = ApiClient
                    .githubService
                    .getFollowingUser(username)

                emit(response)
            }.onStart {
                resultFollowingUser.value = MyResult.Loading(true)
            }.onCompletion {
                resultFollowingUser.value = MyResult.Loading(false)
            }.catch {
                Log.e("Error", it.message.toString())
                it.printStackTrace()
                resultFollowingUser.value = MyResult.Error(it)
            }.collect {
                resultFollowingUser.value = MyResult.Success(it)
            }
        }
    }

    class Factory(private val db: DbModule) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = DetailViewModel(db) as T
    }
}