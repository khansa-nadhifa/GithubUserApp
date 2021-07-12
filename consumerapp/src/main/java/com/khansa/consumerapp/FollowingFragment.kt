package com.khansa.consumerapp


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.khansa.consumerapp.databinding.FragmentFollowingBinding

class FollowingFragment : Fragment() {

    private var binding: FragmentFollowingBinding? = null
    private lateinit var adapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel
    private val _binding get() = binding!!

    companion object {
        const val EXTRA_DETAIL = "extra_detail"

        fun newInstance(dataUsername: String): FollowingFragment{
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_DETAIL, dataUsername)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataUsername = arguments?.getString(EXTRA_DETAIL)
        setRecyclerView(dataUsername)
        Log.d("following user", dataUsername.toString())

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        if (dataUsername != null) {
            showLoading(true)
            mainViewModel.setFollowingUser(dataUsername)
        }

        mainViewModel.getFollowingUser().observe(viewLifecycleOwner, {
                following -> if (following != null) {
            adapter.setData(following)
            showLoading(false)
        }
        })
    }

    private fun setRecyclerView(dataUsername: String?) {

        adapter = UserAdapter(mutableListOf())
        adapter.notifyDataSetChanged()

        binding?.rvFollowing?.layoutManager = LinearLayoutManager(activity)
        binding?.rvFollowing?.adapter = adapter
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
    }
}