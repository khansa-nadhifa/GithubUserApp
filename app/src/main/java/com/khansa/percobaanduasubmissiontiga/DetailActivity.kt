package com.khansa.percobaanduasubmissiontiga

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.khansa.percobaanduasubmissiontiga.databinding.ActivityDetailBinding
import com.khansa.percobaanduasubmissiontiga.db.DatabaseContract.UserColumns.Companion.AVATAR_URL
import com.khansa.percobaanduasubmissiontiga.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
//import com.khansa.percobaanduasubmissiontiga.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.khansa.percobaanduasubmissiontiga.db.DatabaseContract.UserColumns.Companion.USERNAME
import com.khansa.percobaanduasubmissiontiga.db.DatabaseContract.UserColumns.Companion._ID
import com.khansa.percobaanduasubmissiontiga.db.UserHelper
import com.khansa.percobaanduasubmissiontiga.entity.DataUser
import com.khansa.percobaanduasubmissiontiga.helper.MappingHelper

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var dataUser: DataUser
    private var position: Int = 0
    private var isFavorite = false
    private lateinit var uriWithId: Uri

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )

        //favorite
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val RESULT_DELETE = 301
        const val EXTRA_DATA = "extra_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Detail Profile"


        val dataUsername = intent.getStringExtra(EXTRA_DETAIL)
        dataUser = intent.getParcelableExtra<DataUser>(EXTRA_DATA) as DataUser

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                MainViewModel::class.java)

        showLoading(true)
        if (dataUsername != null) {
            mainViewModel.setDetailUser(dataUsername.toString())
        }

        mainViewModel.getDetailUser().observe(this, {
            if (it != null) {
                binding.tvDetailName.text = it.username
                Glide.with(this)
                        .load(it.photo)
                        .into(binding.imgDetailPhoto)
                binding.tvFollowerCount.text = it.follower.toString()
                binding.tvFollowingCount.text = it.following.toString()

                showLoading(false)
            }
            Log.d("ada data", it.toString())
        })

        val sectionsPagerAdapter = dataUsername?.let { SectionsPagerAdapter(this, it) }
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        Log.d("zhongli haver", dataUser.toString())

        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + dataUser.id)

        //favorite
        dataUsername?.let { isFavorite(dataUser) }
        dataUsername?.let { addFavorite(dataUser) }

    }

    private fun isFavorite(dataUsername: DataUser) {
        val cursor = contentResolver.query(uriWithId, null, null, null, null)

        val userData = MappingHelper.mapCursorToObject(cursor)
        Log.d("USER: " , userData.toString())

        if (userData.id == 0) {
            isFavorite = false
            setStatusFavorite(isFavorite)
            cursor!!.close()
        } else {
            isFavorite = true
            setStatusFavorite(isFavorite)
            cursor!!.close()
        }
    }

    private fun addFavorite(dataUsername: DataUser) {
        binding.imgFavorite.setOnClickListener {
            if (isFavorite) {

                contentResolver.delete(uriWithId, null, null)

                isFavorite = !isFavorite
                setStatusFavorite(isFavorite)

                Toast.makeText(this, "Anda tidak menyukai profile ini.", Toast.LENGTH_SHORT).show()
            } else {
                val values = ContentValues()
                values.put(_ID, dataUsername.id)
                values.put(USERNAME, dataUsername.username)
                values.put(AVATAR_URL, dataUsername.photo)

                contentResolver.insert(CONTENT_URI, values)
                isFavorite = !isFavorite
                setStatusFavorite(isFavorite)
                Toast.makeText(this, "Anda menyukai profile ini.", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if(statusFavorite) {
            binding.imgFavorite.setImageResource(R.drawable.baseline_favorite_white_48dp)
        } else {
            binding.imgFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_white_48dp)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}


