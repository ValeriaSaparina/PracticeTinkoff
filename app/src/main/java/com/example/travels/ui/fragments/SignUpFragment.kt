package com.example.travels.ui.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.example.travels.R
import com.example.travels.databinding.FragmentSignUpBinding
import com.example.travels.ui.BaseFragment
import com.example.travels.ui.viewmodels.SignUpViewModel
import com.example.travels.utils.Regexes

class SignUpFragment : BaseFragment() {
    private var viewBinding: FragmentSignUpBinding? = null

    private val viewModel: SignUpViewModel by viewModels {
        SignUpViewModel.Factory
    }


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
            error.observe(viewLifecycleOwner) {
                if (it != null) {
                    showToast(it.message!!)
                }
            }
        }
    }

    private fun initListener() {
        viewBinding?.run {
            signUpBtn.setOnClickListener {
                if (isValidData()) {
                    if (viewModel.signingUp.value != true) {
                        viewModel.onSignUpClick(
                            emailEt.text.toString(),
                            firstnameEt.text.toString(),
                            lastnameEt.text.toString(),
                            passwordEt.text.toString()
                        )
                    } else {
                        showToast("Please wait")
                    }
                } else {
                    showToast(getString(R.string.invalid_data))
                }
            }

            emailEt.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && !isValidEmail()) {
                    (view as? EditText)?.error = getString(R.string.input_correct_email)
                }
            }

            passwordEt.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && !isValidPassword()) {
                    (view as? EditText)?.error = getString(R.string.short_password)
                }
            }

            confirmPasswordEt.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && !isSamePassword()) {
                    (view as? EditText)?.error = getString(R.string.different_passwords)
                }
            }

            firstnameEt.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && !isValidFirstname()) {
                    (view as EditText).error = getString(R.string.input_correct_firstname)
                }
            }

            lastnameEt.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && !isValidLastname()) {
                    (view as EditText).error = getString(R.string.input_correct_lastname)
                }
            }

        }
    }


    private fun isValidEmail(): Boolean {
        viewBinding?.emailEt?.run {
            return Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()
        }
        throw IllegalStateException("viewBinding is not initialized")
    }

    private fun isValidPassword(): Boolean {
        viewBinding?.passwordEt?.run {
            return text.toString().length >= 8
        }
        throw IllegalStateException("viewBinding is not initialized")
    }

    private fun isSamePassword(): Boolean {
        viewBinding?.run {
            return passwordEt.text.toString() == confirmPasswordEt.text.toString()
        }
        throw IllegalStateException("viewBinding is not initialized")
    }

    private fun isValidData(): Boolean {
        return isValidEmail() && isValidPassword() && isSamePassword() && isValidFirstname() && isValidLastname()
    }

    private fun isValidFirstname(): Boolean {
        return viewBinding?.firstnameEt?.text?.matches(Regexes.nameRegex) ?: false
    }

    private fun isValidLastname(): Boolean {
        return viewBinding?.lastnameEt?.text?.matches(Regexes.nameRegex) ?: false
    }


}