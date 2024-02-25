package com.example.travels.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.travels.databinding.FragmentSignInBinding
import com.example.travels.ui.BaseFragment
import com.example.travels.ui.viewmodels.SignInViewModel

class SignInFragment : BaseFragment() {
    private var viewBinding: FragmentSignInBinding? = null
    private val viewModel: SignInViewModel by viewModels {
        SignInViewModel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSignInBinding.inflate(inflater)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()
        initListeners()
    }

    private fun initListeners() {
        viewBinding?.apply {
            signInBtn.setOnClickListener {
                if (viewModel.signingIn.value != true) {
                    viewModel.onSignUpClick(emailEt.text.toString(), passwordEt.text.toString())
                }
            }
        }
    }

    private fun observe() {
        with(viewModel) {
            error.observe(viewLifecycleOwner) {
                if (it != null) {
                    showToast(it.message!!)
                }
            }
        }
    }
}