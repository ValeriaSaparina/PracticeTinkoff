package com.example.travels.ui.settings.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.travels.R
import com.example.travels.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class LanguageDialogFragment : DialogFragment() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.choice_the_language)
        builder.setItems(
            arrayOf(
                resources.getString(R.string.russian),
                resources.getString(R.string.english)
            )
        ) { dialog, which ->
            when (which) {
                0 -> {
                    selectLanguage("ru")
                }

                1 -> {
                    selectLanguage("en_US")
                }
            }
        }
        return builder.create()
    }

    private fun selectLanguage(language: String) {
        if (language == "ru") {
            val locale = Locale(language)
            Locale.setDefault(locale)
            requireContext().createConfigurationContext(requireActivity().resources.configuration.apply {
                setLocale(
                    locale
                )
            })
        } else {
            requireContext().createConfigurationContext(requireActivity().resources.configuration.apply { setToDefaults() })
        }
        requireActivity().recreate()
        val editor = sharedPreferences.edit()
        editor.putString(Constants.LANGUAGE, language).apply()
    }
}
