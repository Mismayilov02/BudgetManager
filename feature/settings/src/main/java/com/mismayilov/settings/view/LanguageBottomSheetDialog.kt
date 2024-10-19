package com.mismayilov.settings.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mismayilov.data.local.SharedPreferencesManager
import com.mismayilov.manager.LanguageManager.setLocale
import com.mismayilov.settings.LanguageAdapter
import com.mismayilov.settings.databinding.LanguageBottomSheetDialogBinding
class LanguageBottomSheetDialog constructor(
    private val selectedLanguagesCode:String) : BottomSheetDialogFragment() {
    private var _binding: LanguageBottomSheetDialogBinding? = null
    private var selectedPosition = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LanguageBottomSheetDialogBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.iconRecyclerView?.setHasFixedSize(true)
        initRecycler()
    }

    private fun initRecycler() {
        val list: List<Pair<String,String>> = getLanguages()
        val adapter =  LanguageAdapter(selectedPosition)
        _binding?.apply {
            iconRecyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 1)
            iconRecyclerView.adapter = adapter
            btnSelectIcon.setOnClickListener {
                SharedPreferencesManager.setValue("languageCode", list[adapter.selectedPosition].second)
                setLocale(requireContext(), list[adapter.selectedPosition].second)
                activity?.recreate()
                dismiss()
            }
            adapter.submitList(list)
        }
    }

    private fun getLanguages(): MutableList<Pair<String,String>> {
        val languages = mutableListOf<Pair<String,String>>()
        val languageCodes = resources.getStringArray(com.mismayilov.uikit.R.array.languages_code)
        val languageNames = resources.getStringArray(com.mismayilov.uikit.R.array.languages)
        for (i in languageCodes.indices) {
            languages.add(Pair(languageNames[i],languageCodes[i]))
            if (languageCodes[i] == selectedLanguagesCode) selectedPosition = i
        }
        return languages
    }

}