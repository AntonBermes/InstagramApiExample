package com.antonbermes.instagramapiexample.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.antonbermes.instagramapiexample.R
import com.antonbermes.instagramapiexample.database.ImagesDatabase
import com.antonbermes.instagramapiexample.databinding.FragmentDetailsBinding
import com.antonbermes.instagramapiexample.repository.Status
import com.antonbermes.instagramapiexample.ui.MyFragment

class DetailsFragment : MyFragment() {

    private val dataSource by lazy { ImagesDatabase.getInstance(application).imagesDao }
    private val token by lazy { arguments?.let { DetailsFragmentArgs.fromBundle(it).token } ?: "" }
    private val imageId by lazy {
        arguments?.let { DetailsFragmentArgs.fromBundle(it).imageId } ?: ""
    }
    private val viewModelFactory by lazy { DetailsViewModelFactory(dataSource, token, imageId) }
    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(DetailsViewModel::class.java)
    }
    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
        viewModel.status.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it) {
                    is Status.StartLoading -> {
                        binding.swipeRefreshLayout.isRefreshing = true
                        dismissSnackbar()
                    }
                    is Status.StopLoading -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                    is Status.Error -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        showSnackbar(it.error, binding.coordinatorLayout)
                    }
                }
            }
        })
        return binding.root
    }

    override fun snackbarAction() {
        viewModel.refresh()
    }


}
