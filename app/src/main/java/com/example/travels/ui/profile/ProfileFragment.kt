package com.example.travels.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.travels.databinding.FragmentProfileBinding
import com.example.travels.domain.auth.model.UserModel
import com.example.travels.ui.base.BaseFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {
    private var viewBinding: FragmentProfileBinding? = null
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentProfileBinding.inflate(inflater)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        viewModel.loadUserProfile(Firebase.auth.uid ?: "")
    }

    private fun observe() {
        with(viewModel) {
            user.observe {
                if (it != null) {
                    showData(it)
                }
            }
            error.observe {
                if (it != null) {
                    showToast(it.name)
                }
            }
        }
    }

    private fun showData(user: UserModel) {
        viewBinding?.run {
            with(this) {

            }
        }
    }
}