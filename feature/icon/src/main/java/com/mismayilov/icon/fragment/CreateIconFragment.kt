package com.mismayilov.icon.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.mismayilov.uikit.adapter.IconListRecyclerAdapter
import com.mismayilov.core.base.fragment.BaseFragment
import com.mismayilov.core.managers.NavigationManager
import com.mismayilov.domain.entities.local.IconModel
import com.mismayilov.icon.databinding.FragmentCreateIconBinding
import com.mismayilov.icon.flow.create_icon.CreateIconEffect
import com.mismayilov.icon.flow.create_icon.CreateIconEvent
import com.mismayilov.icon.flow.create_icon.CreateIconState
import com.mismayilov.icon.viewmodel.CreateIconViewModel
import com.mismayilov.uikit.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateIconFragment :
    BaseFragment<FragmentCreateIconBinding, CreateIconViewModel, CreateIconState, CreateIconEvent, CreateIconEffect>() {
    override fun getViewModelClass(): Class<CreateIconViewModel> = CreateIconViewModel::class.java
    override val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreateIconBinding =
        { inflater, container, attachToRoot ->
            FragmentCreateIconBinding.inflate(inflater, container, attachToRoot)
        }

    private val args: CreateIconFragmentArgs by navArgs()
    private val adapter = IconListRecyclerAdapter(showItemName = false)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initClickListeners()
    }

    private fun checkEditMode(icons: List<IconModel>) {
        if (args.isEdit) {
            binding.btnAdd.text = getString(com.mismayilov.uikit.R.string.update)
            binding.iconNameTxt.setText(args.name)
            adapter.selectedPosition = icons.indexOfFirst {
                it.icon == args.icon
            }
        }
    }

    private fun initClickListeners() {
        binding.btnAdd.setOnClickListener {
            setEvent(
                CreateIconEvent.AddIconClicked(
                    binding.iconNameTxt.text.toString(),
                    adapter.selectedPosition,
                    args.iconType,
                    args.isEdit,
                    args.id
                )
            )
        }

        binding.customTopBar.backClickListener = {
            (activity as NavigationManager).back()
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
            recyclerView.adapter = adapter
        }
    }

    override fun renderEffect(effect: CreateIconEffect) {
        when (effect) {
            is CreateIconEffect.ShowToast -> showToast(effect.message)
            is CreateIconEffect.CloseFragment -> (activity as NavigationManager).back()
        }
    }

    override fun renderState(state: CreateIconState) {
        state.icons?.let {
            checkEditMode(it)
            adapter.submitList(it)
        }
    }
}