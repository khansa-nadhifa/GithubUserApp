package com.khansa.percobaanduasubmissiontiga

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khansa.percobaanduasubmissiontiga.entity.DataUser
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainViewModel : ViewModel() {

    private val listUser = MutableLiveData<ArrayList<DataUser>>()
    private val detailUser = MutableLiveData<DataUser>()
    private val followerUser = MutableLiveData<ArrayList<DataUser>>()
    private val followingUser = MutableLiveData<ArrayList<DataUser>>()

    fun setUser(username: String) {
        val listItems = ArrayList<DataUser>()
        val apiKey = "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"
        val url = "https://api.github.com/search/users?q=$username"

        val client = AsyncHttpClient()

        client.addHeader("Authorization", "token.$apiKey")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    //parsing json
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("items")

                    for (i in 0 until list.length()) {
                        val item = list.getJSONObject(i)
                        val username = item.getString("login")
                        val photo = item.getString("avatar_url")
                        val id = item.getInt("id")

                        listItems.add(DataUser(id, photo, username))
                    }
                    Log.d("nama user", result)
                    listUser.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun setDetailUser(username: String?) {
        val apiKey = "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"
        val url = " https://api.github.com/users/$username"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token.$apiKey")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    //parsing json
                    val result = String(responseBody)
                    val item = JSONObject(result)
                    val username = item.getString("login")
                    val photo = item.getString("avatar_url")
                    val id = item.getInt("id")
                    val follower = item.getInt("followers")
                    val following = item.getInt("following")
                    detailUser.postValue(DataUser(id, photo, username, follower, following))
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun setFollowerUser(username: String) {
        val listItems = ArrayList<DataUser>()
        val apiKey = "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"
        val url = "https://api.github.com/users/$username/followers"

        val client = AsyncHttpClient()

        client.addHeader("Authorization", "token.$apiKey")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    //parsing json
                    val result = String(responseBody)
                    val responseObject = JSONArray(result)

                    for (i in 0 until responseObject.length()) {
                        val item = responseObject.getJSONObject(i)
                        val username = item.getString("login")
                        val photo = item.getString("avatar_url")
                        val id = item.getInt("id")

                        listItems.add(DataUser(id, photo, username))

                        Log.d("follower user", username.toString())
                    }
                    followerUser.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun setFollowingUser(username: String) {
        val listItems = ArrayList<DataUser>()
        val apiKey = "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"
        val url = "https://api.github.com/users/$username/following"

        val client = AsyncHttpClient()

        client.addHeader("Authorization", "token.$apiKey")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    //parsing json
                    val result = String(responseBody)
                    val responseObject = JSONArray(result)

                    for (i in 0 until responseObject.length()) {
                        val item = responseObject.getJSONObject(i)
                        val username = item.getString("login")
                        val photo = item.getString("avatar_url")
                        val id = item.getInt("id")

                        listItems.add(DataUser(id, photo, username))

                        Log.d("following user", username.toString())
                    }
                    followingUser.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }


    fun getUser(): LiveData<ArrayList<DataUser>> {
        return listUser
    }

    fun getDetailUser(): LiveData<DataUser> {
        return detailUser
    }

    fun getFollowerUser(): LiveData<ArrayList<DataUser>> {
        return followerUser
    }

    fun getFollowingUser(): LiveData<ArrayList<DataUser>> {
        return followingUser
    }
}