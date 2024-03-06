package com.example.travels.ui

import android.widget.Toast
import androidx.fragment.app.Fragment
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

}