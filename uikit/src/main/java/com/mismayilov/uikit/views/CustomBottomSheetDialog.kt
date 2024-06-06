package com.mismayilov.uikit.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.uikit.adapter.AccountListRecyclerAdapter
import com.mismayilov.uikit.adapter.IconListRecyclerAdapter
import com.mismayilov.uikit.databinding.CustomBottomSheetDialogBinding

class CustomBottomSheetDialog constructor(private val icons:List<IconModel>, private val onSelected: ((Long) -> Unit),
                                          private val addIconSelected:(()->Unit)) : BottomSheetDialogFragment() {
    private var _binding: CustomBottomSheetDialogBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CustomBottomSheetDialogBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.iconRecyclerView?.setHasFixedSize(true)
        if (icons[0].balance!=null) initAccountRecycler() else initIconRecycler()

        _binding?.btnAdd?.setOnClickListener {
            addIconSelected.invoke()
            dismiss()
        }
    }

    private fun initAccountRecycler() {
        val adapter =  AccountListRecyclerAdapter()
        _binding?.apply {
            iconRecyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 1)
            iconRecyclerView.adapter = adapter
            btnSelectIcon.setOnClickListener {
                onSelected.invoke(icons[adapter.selectedPosition].id)
                dismiss()
            }
            adapter.submitList(icons)
        }
    }

    private fun initIconRecycler() {
       val adapter =  IconListRecyclerAdapter()
        _binding?.apply {
            iconRecyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 4)
            iconRecyclerView.adapter = adapter
            btnSelectIcon.setOnClickListener {
                onSelected.invoke(icons[adapter.selectedPosition].id)
                dismiss()
            }
            adapter.submitList(icons)
        }

    }

}