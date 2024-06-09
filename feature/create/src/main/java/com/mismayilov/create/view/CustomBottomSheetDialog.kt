package com.mismayilov.create.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mismayilov.create.databinding.CustomBottomSheetDialogBinding
import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.create.adapter.AccountListRecyclerAdapter
import com.mismayilov.uikit.adapter.IconListRecyclerAdapter
import kotlin.properties.Delegates

class CustomBottomSheetDialog constructor(val showBalance:Boolean = false,private val icons:List<IconModel>, private val onSelected: ((Long) -> Unit),
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
        val adapter =  AccountListRecyclerAdapter(showBalance)
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