package com.example.travels.ui

import android.widget.Toast
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    protected fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), message, duration).show()
    }
}