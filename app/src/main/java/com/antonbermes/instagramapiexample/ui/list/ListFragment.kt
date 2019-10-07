package com.antonbermes.instagramapiexample.ui.list

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.antonbermes.instagramapiexample.R
import com.antonbermes.instagramapiexample.database.ImagesDatabase
import com.antonbermes.instagramapiexample.databinding.FragmentListBinding
import com.antonbermes.instagramapiexample.domain.Type
import com.antonbermes.instagramapiexample.repository.Status
import com.antonbermes.instagramapiexample.ui.MyFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : MyFragment() {

    private val dataSource by lazy { ImagesDatabase.getInstance(application).imagesDao }
    private val token by lazy { arguments?.let { ListFragmentArgs.fromBundle(it).token } ?: "" }
    private val viewModelFactory by lazy { ListViewModelFactory(dataSource, token) }
    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ListViewModel::class.java)
    }
    private lateinit var binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val adapter = MyListAdapter(ListListener { image -> viewModel.listClicked(image) })
        binding.recycler.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.update()
        }
        if (viewModel.status.value is Status.Error) viewModel.retry()
        viewModel.status.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Status.StartLoading -> {
                        binding.swipeRefreshLayout.isRefreshing = true
                        dismissSnackbar()
                    }
                    is Status.StopLoading -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        dismissSnackbar()
                    }
                    is Status.Error -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        showSnackbar(it.error, binding.coordinatorLayout)
                    }
                }
            }
        })
        viewModel.navigateFlag.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.type == Type.IMAGE) {
                    navigate(it.imageId, token)
                    viewModel.navigated()
                } else Snackbar.make(coordinatorLayout, getString(R.string.not_supported), Snackbar.LENGTH_LONG).show()
            }
        })
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        this.findNavController()
            .navigate(ListFragmentDirections.actionListFragmentToAuthorizationFragment(true))
        return true
    }

    override fun snackbarAction() {
        viewModel.retry()
    }

    private fun navigate(imageId: String, token: String) {
        this.findNavController()
            .navigate(ListFragmentDirections.actionListFragmentToDetailsFragment(imageId, token))
    }
}
