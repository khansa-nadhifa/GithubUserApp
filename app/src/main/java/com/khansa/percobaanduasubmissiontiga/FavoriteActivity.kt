package com.khansa.percobaanduasubmissiontiga

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.khansa.percobaanduasubmissiontiga.DetailActivity.Companion.EXTRA_DETAIL
import com.khansa.percobaanduasubmissiontiga.DetailActivity.Companion.EXTRA_POSITION
import com.khansa.percobaanduasubmissiontiga.databinding.ActivityFavoriteBinding
import com.khansa.percobaanduasubmissiontiga.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.khansa.percobaanduasubmissiontiga.entity.DataUser
import com.khansa.percobaanduasubmissiontiga.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.ArrayList


// buat nampilin halaman favorite activity doang

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var mainViewModel: MainViewModel

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "List Favorite User"

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                MainViewModel::class.java)

        showLoading(true)

        adapter = UserAdapter(mutableListOf())

        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.adapter = adapter

        adapter.setOnClickCallback(object : UserAdapter.OnSetClickCallback {
            override fun onClicked(dataUser: DataUser, position: Int) {
                val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                intent.putExtra(EXTRA_POSITION, position)
                intent.putExtra(EXTRA_DETAIL, dataUser.username)
                intent.putExtra(DetailActivity.EXTRA_DATA, dataUser)
                startActivity(intent)
            }
        })

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver: ContentObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadUsersAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)


        if (savedInstanceState == null) {
            loadUsersAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<DataUser>(EXTRA_STATE)

            if (list != null) {
                adapter.data = list.toMutableList()
            }
        }

        mainViewModel.getUser().observe(this, {
            if (it != null ) {
                adapter.setData(it)
                showLoading(false)
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            when (requestCode) {
                DetailActivity.REQUEST_ADD -> if (resultCode == DetailActivity.RESULT_ADD) {
                    val user = data.getParcelableExtra<DataUser>(DetailActivity.EXTRA_POSITION) as DataUser

                    adapter.addItem(user)
                    binding.rvFavorite.smoothScrollToPosition(adapter.itemCount -1)

                }
                DetailActivity.RESULT_DELETE -> {
                    val position = data.getIntExtra(EXTRA_POSITION, 0)
                    adapter.removeItem(position)
                }
            }
        }
    }

    private fun loadUsersAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredUser = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val userFavorite = deferredUser.await()
            if (userFavorite.size > 0) {
                adapter.setData(userFavorite)
                binding.progressBar.visibility = View.GONE
            } else {
                adapter.data = ArrayList()
            }

            Log.d("kaeya", userFavorite.toString() )
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.data as ArrayList<DataUser>)
    }
}