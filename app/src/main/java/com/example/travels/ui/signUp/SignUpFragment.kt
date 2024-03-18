package com.example.travels.ui.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.travels.R
import com.example.travels.databinding.FragmentSignUpBinding
import com.example.travels.ui.App.Companion.router
import com.example.travels.ui.Screens
import com.example.travels.ui.base.BaseFragment
import com.example.travels.utils.validate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment() {
    private var viewBinding: FragmentSignUpBinding? = null


    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSignUpBinding.inflate(inflater)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        initListener()
    }

    private fun observe() {
        with(viewModel) {
            error.observe {
                if (it != null) {
                    showAuthError(it)
                } else {
//                    router.newRootScreen(Screens.Places())
                }
            }
        }
    }

    private fun initListener() {
        viewBinding?.run {
            signUpBtn.setOnClickListener {
                viewModel.onSignUpClick(
                    emailEt.text.toString(),
                    firstnameEt.text.toString(),
                    lastnameEt.text.toString(),
                    passwordEt.text.toString(),
                    confirmPasswordEt.text.toString()
                )
            }

            signInTv.setOnClickListener {
                router.newRootScreen(Screens.SignIn())
            }

            emailEt.validate(
                { text -> viewModel.isValidEmail(text) },
                getString(R.string.input_correct_email)
            )
            passwordEt.validate(
                { text -> viewModel.isValidPassword(text) },
                getString(R.string.short_password)
            )
            confirmPasswordEt.validate({ text ->
                viewModel.isSamePassword(
                    text,
                    passwordEt.text.toString()
                )
            }, getString(R.string.different_passwords))
            firstnameEt.validate(
                { text -> viewModel.isValidName(text) },
                getString(R.string.input_correct_firstname)
            )
            lastnameEt.validate(
                { text -> viewModel.isValidName(text) },
                getString(R.string.input_correct_lastname)
            )
        }
    }

}