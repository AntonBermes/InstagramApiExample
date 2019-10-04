package com.antonbermes.instagramapiexample.ui.list


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.antonbermes.instagramapiexample.R

/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {
    private val token by lazy { arguments?.let { ListFragmentArgs.fromBundle(it).token } ?: "" }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("ListFragment", "token: $token")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }


}
