package com.example.travels.ui.base

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.travels.R
import com.example.travels.utils.AuthErrors
import com.example.travels.utils.observe
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

open class BaseFragment : Fragment() {
    protected fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), message, duration).show()
    }

    inline fun <T> Flow<T>.observe(crossinline block: (T) -> Unit): Job {
        return observe(fragment = this@BaseFragment, block)
    }

    protected fun showAuthError(error: AuthErrors) {
        when (error) {
            AuthErrors.WAIT -> showToast(getString(R.string.wait_error))
            AuthErrors.INVALID_DATA -> showToast(getString(R.string.invalid_data))
            AuthErrors.UNEXPECTED -> showToast(getString(R.string.unexpected_error))
            AuthErrors.INVALID_CREDENTIALS -> showToast(getString(R.string.invalid_credentials))
        }
    }

}