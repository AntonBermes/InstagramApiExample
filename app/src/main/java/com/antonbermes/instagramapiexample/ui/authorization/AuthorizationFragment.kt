package com.antonbermes.instagramapiexample.ui.authorization

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.antonbermes.instagramapiexample.R
import com.antonbermes.instagramapiexample.databinding.FragmentAuthorizationBinding
import com.antonbermes.instagramapiexample.repository.Status
import com.antonbermes.instagramapiexample.ui.MyFragment
import com.antonbermes.instagramapiexample.util.isConnected

class AuthorizationFragment : MyFragment() {

    private val isNewToken by lazy {
        arguments?.let { AuthorizationFragmentArgs.fromBundle(it).isNewToken } ?: false
    }
    private val viewModelFactory by lazy { AuthorizationViewModelFactory(application, isNewToken) }
    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(AuthorizationViewModel::class.java)
    }
    private lateinit var binding: FragmentAuthorizationBinding
    private var isInitWebView = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val token = viewModel.accessToken.value
        if (token != null) {
            navigate(token)
            return null
        }

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_authorization, container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.accessToken.observe(viewLifecycleOwner, Observer { it?.let { navigate(it) } })
        viewModel.status.observe(viewLifecycleOwner, Observer { it?.let { setStatus(it) } })
        binding.swipeRefreshLayout.setOnRefreshListener {
            dismissSnackbar()
            checkInternetAndDownloadWebPage()
        }

        @Suppress("DEPRECATION")
        if (savedInstanceState == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                CookieManager.getInstance().removeAllCookies(null)
            } else {
                CookieManager.getInstance().removeAllCookie()
            }
        }
        checkInternetAndDownloadWebPage()
        return binding.root
    }

    override fun snackbarAction() {
        binding.swipeRefreshLayout.isRefreshing = true
        checkInternetAndDownloadWebPage()
    }

    private fun checkInternetAndDownloadWebPage() {
        if (isConnected(application)) {
            showWeb()
        } else {
            showSnackbar(getString(R.string.no_internet), binding.coordinatorLayout)
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun showWeb() {
        if (!isInitWebView) {
            binding.webView.apply {
                webViewClient = MyWebViewClient({ status -> viewModel.setStatus(status) },
                    { url -> viewModel.checkUrl(url) })
                settings.javaScriptEnabled = true
                loadUrl(viewModel.currentUrl)
            }
            isInitWebView = true
        } else binding.webView.loadUrl(viewModel.currentUrl)
    }

    private fun navigate(token: String) {
        this.findNavController().navigate(
            AuthorizationFragmentDirections.actionAuthorizationFragmentToListFragment(token)
        )
    }

    private fun setStatus(status: Status) {
        when (status) {
            is Status.StartLoading -> {
                dismissSnackbar()
                binding.swipeRefreshLayout.isRefreshing = true
                binding.webView.visibility = View.GONE
            }
            is Status.StopLoading -> {
                binding.swipeRefreshLayout.isRefreshing = false
                binding.webView.visibility = View.VISIBLE
            }
            is Status.Error -> {
                showSnackbar(status.error, binding.coordinatorLayout)
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}
