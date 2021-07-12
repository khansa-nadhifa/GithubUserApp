package com.khansa.percobaanduasubmissiontiga

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.khansa.percobaanduasubmissiontiga.databinding.FragmentFollowerBinding
import com.khansa.percobaanduasubmissiontiga.databinding.FragmentFollowerBinding.inflate

class FollowerFragment : Fragment() {

    private var binding: FragmentFollowerBinding? = null
    private lateinit var adapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel
    private val _binding get() = binding!!

    companion object {

        const val EXTRA_DETAIL = "extra_detail"

        fun newInstance(dataUsername: String): FollowerFragment{
            val fragment = FollowerFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_DETAIL, dataUsername)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataUsername = arguments?.getString(EXTRA_DETAIL)
        setRecyclerView(dataUsername)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        if (dataUsername != null) {
            showLoading(true)
            mainViewModel.setFollowerUser(dataUsername)
        }

        mainViewModel.getFollowerUser().observe(viewLifecycleOwner, {
                follower -> if (follower != null) {
            adapter.setData(follower)
            showLoading(false)
            Log.d("follower user", dataUsername.toString())
        }
        })
    }

    private fun setRecyclerView(dataUser: String?) {
        adapter = UserAdapter(mutableListOf())
        adapter.notifyDataSetChanged()

        binding?.rvFollower?.layoutManager = LinearLayoutManager(activity)
        binding?.rvFollower?.adapter = adapter
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
    }
}