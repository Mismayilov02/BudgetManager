package com.mismayilov.settings.fragment

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import androidx.core.view.isVisible
import com.mismayilov.common.utility.Util.Companion.TIME_FORMAT
import com.mismayilov.common.utility.showTimePicker
import com.mismayilov.core.managers.NavigationManager
import com.mismayilov.core.managers.TwoFactorAuthManager
import com.mismayilov.data.local.SharedPreferencesManager
import com.mismayilov.manager.LanguageManager
import com.mismayilov.manager.LanguageManager.setLocale
import com.mismayilov.manager.ReminderManager
import com.mismayilov.settings.R
import com.mismayilov.settings.databinding.FragmentSettingsBinding
import com.mismayilov.settings.view.LanguageBottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val simpleDateFormat = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())

    @Inject
    lateinit var reminderManager: ReminderManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initReminder()
        initClickListeners()
        initBalanceSwitch()
        initTwoFactorAuth()
        initDarkModeSwitch()
        initLanguage()
    }

    private fun initLanguage() {
        _binding?.apply {
            val languageCode = SharedPreferencesManager.getValue("languageCode", "en")
            val languagePosition = LanguageManager.getLanguagePosition(languageCode)
            val languageNames = resources.getStringArray(com.mismayilov.uikit.R.array.languages)[languagePosition]
            languageItemView.setSubText(languageNames)
            languageItemView.setOnClickListener {
                val bottomSheetDialog = LanguageBottomSheetDialog(languageCode)
                bottomSheetDialog.show(childFragmentManager, "languageBottomSheet")
            }
        }
    }

    private fun initDarkModeSwitch() {
        _binding!!.darkModeSwitch.isChecked = SharedPreferencesManager.getValue("darkMode", false)
        _binding!!.darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            SharedPreferencesManager.setValue("darkMode", isChecked)
            AppCompatDelegate.setDefaultNightMode(if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun initTwoFactorAuth() {
        _binding!!.otpSwitch.isChecked = SharedPreferencesManager.getValue("twoFactorAuth", false)
        _binding!!.otpSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                (activity as NavigationManager).navigateByNavigationName("pinFragment")
            } else {
                (activity as TwoFactorAuthManager).setTwoFactorAuthEnabled(false)
            }
        }
    }

    private fun initBalanceSwitch() {
        val showBalance = SharedPreferencesManager.getValue("hideBalance", false)
        _binding!!.balanceSwitch.isChecked = showBalance
        _binding!!.balanceSwitch.setOnCheckedChangeListener { _, isChecked ->
            SharedPreferencesManager.setValue("hideBalance", isChecked)
        }
    }

    private fun initReminder() {
        _binding?.apply {
            reminderSwitch.isChecked = SharedPreferencesManager.getValue("reminder", false)
            reminderSubtext.text =
                simpleDateFormat.format(SharedPreferencesManager.getValue("reminderTime", 0L))
            reminderSwitch.setOnCheckedChangeListener { _, isChecked ->
                handleReminderSwitch(isChecked, reminderSubtext)
            }
        }
    }

    private fun handleReminderSwitch(isChecked: Boolean, textView: TextView) {
        if (isChecked) {
            showTimePicker(requireContext(), onTimeSelected = {
                SharedPreferencesManager.setValue("reminderTime", it)
                SharedPreferencesManager.setValue("reminder", true)
                textView.text = simpleDateFormat.format(it)
                textView.isVisible = true
                reminderManager.scheduleDailyReminder(it)
            }, onCancel = {
                _binding!!.reminderSwitch.isChecked = false
            })
        } else {
            reminderManager.cancelDailyReminder()
            SharedPreferencesManager.setValue("reminder", false)
            textView.isVisible = false
        }
    }

    private fun initClickListeners() {
        _binding?.apply {
            accountItemView.setOnClickListener {
                (activity as NavigationManager).navigateByNavigationName("account_navigation")
            }
            iconItemView.setOnClickListener {
                (activity as NavigationManager).navigateByNavigationName("icon_navigation")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        (activity as NavigationManager).bottomNavigationVisibility(true)
    }

   /* private fun showPopupMenu(view: View, languages: Array<String>) {
        val popupMenu = PopupMenu(requireContext(), view)
        languages.forEachIndexed { index, language ->
            popupMenu.menu.add(0, index, 0, language)
        }

        popupMenu.setOnMenuItemClickListener { menuItem ->
            val position = menuItem.itemId
            val selectedLanguage =LanguageManager.getLanguageCodeByPosition(position)
            setLocale(requireContext(), selectedLanguage)
            SharedPreferencesManager.setValue("language", selectedLanguage)
//            saveLanguagePreference(this, selectedLanguage)
//            textView.text = languages[position] // Seçilen dili TextView'de göster
//            requireActivity().recreate() // Aktiviteyi yeniden başlatın
            (activity as NavigationManager).recreateActivity()
            true
        }

        popupMenu.show()
    }*/
}