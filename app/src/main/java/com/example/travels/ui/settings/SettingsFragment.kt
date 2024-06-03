package com.example.travels.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.travels.R
import com.example.travels.databinding.FragmentSettingsBinding
import com.example.travels.ui.MainActivity
import com.example.travels.ui.base.BaseFragment
import com.example.travels.ui.settings.dialogs.LanguageDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private val viewBinding by viewBinding(FragmentSettingsBinding::bind)
    private val viewModel: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).isBottomNavVisible = false
        initListeners()
    }

    private fun initListeners() {
        with(viewBinding) {
            changeLanguageLl.setOnClickListener {
                val languageDialogFragment = LanguageDialogFragment()
                languageDialogFragment.show(parentFragmentManager, "language_dialog")
            }
        }
    }

}