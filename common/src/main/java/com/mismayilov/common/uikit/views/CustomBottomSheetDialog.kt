package com.mismayilov.uikit.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mismayilov.common.databinding.CustomBottomSheetDialogBinding
import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.common.uikit.adapter.IconsRecyclerViewAdapter

class CustomBottomSheetDialog constructor(private val icons:List<IconModel>, private val onSelected: ((Long) -> Unit)): BottomSheetDialogFragment() {
    private var _binding: CustomBottomSheetDialogBinding? = null
    private val adapter = IconsRecyclerViewAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CustomBottomSheetDialogBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        _binding?.btnSelectIcon?.setOnClickListener {
            onSelected.invoke(icons[adapter.selectedPosition].id)
            dismiss()
        }
    }

    private fun initRecyclerView() {
        _binding?.iconRecyclerView?.setHasFixedSize(true)
        _binding?.iconRecyclerView?.layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 4)
        _binding?.iconRecyclerView?.adapter = adapter
        adapter.submitList(icons)
    }

}