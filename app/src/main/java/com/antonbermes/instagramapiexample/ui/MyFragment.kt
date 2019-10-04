package com.antonbermes.instagramapiexample.ui

import android.app.Application
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.antonbermes.instagramapiexample.R
import com.google.android.material.snackbar.Snackbar

abstract class MyFragment : Fragment() {

    protected val application: Application by lazy { requireNotNull(this.activity).application }
    private var snackbar: Snackbar? = null

    protected fun showSnackbar(s: String?, view: CoordinatorLayout) {
        snackbar = Snackbar.make(
            view,
            s.toString(),
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar?.setAction(R.string.retry) {
            snackbarAction()
        }
        snackbar?.show()
    }

    protected fun dismissSnackbar() {
        snackbar?.dismiss()
        snackbar = null
    }

    abstract fun snackbarAction()
}