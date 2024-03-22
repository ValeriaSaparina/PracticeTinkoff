package com.example.travels.ui.signIn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.travels.databinding.FragmentSignInBinding
import com.example.travels.ui.App.Companion.router
import com.example.travels.ui.Screens
import com.example.travels.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment() {
    private var viewBinding: FragmentSignInBinding? = null

    private val viewModel: SignInViewModel by viewModels()

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
                viewModel.onSignUpClick(emailEt.text.toString(), passwordEt.text.toString())
            }

            signUpTv.setOnClickListener {
                router.newRootScreen(Screens.SignUp())
            }
        }
    }

    private fun observe() {
        with(viewModel) {
            error.observe {
                if (it != null) {
                    showAuthError(it)
                }
            }

            success.observe {
                if (it) {
//                    router.newRootScreen(Screens.Places())
                }
            }
        }
    }
}