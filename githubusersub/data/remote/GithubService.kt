package com.example.githubusersub.data.remote

import com.example.githubusersub.BuildConfig
import com.example.githubusersub.data.model.ItemsItem
import com.example.githubusersub.data.model.ResponseDetailUser
import com.example.githubusersub.data.model.ResponseUserGithub
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface GithubService {

    @JvmSuppressWildcards
    @GET("users")
    suspend fun getUserGithub(
        @Header("Authorization")
        authorization: String = BuildConfig.TOKEN
    ): MutableList<ItemsItem>

    @JvmSuppressWildcards
    @GET("users/{username}")
    suspend fun getDetailUser(
        @Path("username") username: String,
        @Header("Authorization")
        authorization: String = BuildConfig.TOKEN
    ): ResponseDetailUser

    @JvmSuppressWildcards
    @GET("/users/{username}/followers")
    suspend fun getFollowersUser(
        @Path("username") username: String,
        @Header("Authorization")
        authorization: String = BuildConfig.TOKEN
    ): MutableList<ItemsItem>

    @JvmSuppressWildcards
    @GET("/users/{username}/following")
    suspend fun getFollowingUser(
        @Path("username") username: String,
        @Header("Authorization")
        authorization: String = BuildConfig.TOKEN
    ): MutableList<ItemsItem>

    @JvmSuppressWildcards
    @GET("search/users")
    suspend fun searchUserGithub(
        @QueryMap(encoded = true) params: Map<String, String>,
        @Header("Authorization")
        authorization: String = BuildConfig.TOKEN
    ): ResponseUserGithub
}