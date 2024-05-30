package com.mismayilov.icon.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.mismayilov.common.unums.IconType
import com.mismayilov.core.base.fragment.BaseFragment
import com.mismayilov.core.managers.NavigationManager
import com.mismayilov.icon.adapter.IconListAdapter
import com.mismayilov.icon.databinding.FragmentIconManagerBinding
import com.mismayilov.icon.flow.icon_manager.IconManagerEffect
import com.mismayilov.icon.flow.icon_manager.IconManagerEvent
import com.mismayilov.icon.flow.icon_manager.IconManagerState
import com.mismayilov.icon.viewmodel.IconManagerViewModel
import com.mismayilov.uikit.util.showDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IconManagerFragment :
    BaseFragment<FragmentIconManagerBinding, IconManagerViewModel, IconManagerState, IconManagerEvent, IconManagerEffect>() {
    override fun getViewModelClass(): Class<IconManagerViewModel> = IconManagerViewModel::class.java
    override val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentIconManagerBinding =
        { inflater, container, attachToRoot ->
            FragmentIconManagerBinding.inflate(inflater, container, attachToRoot)
        }

    private lateinit var iconAdapter: IconListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as NavigationManager).bottomNavigationVisibility(false)
        initAdapter()
        initRecyclerView()
        initTabLayout()
        initClickListeners()
    }

    private fun initClickListeners() {
        binding.apply {
            btnAdd.setOnClickListener {
                val selectedIconType =
                    if (tabLayout.currentSelectedTabPosition == 0) IconType.EXPENSE else IconType.INCOME
                val directions =
                    IconManagerFragmentDirections.actionIconManagerFragmentToCreateIconFragment(
                        iconType = selectedIconType.name
                    )
                (activity as NavigationManager).navigateByDirection(directions)
            }
            customTopBar.backClickListener = {
                (activity as NavigationManager).back()
            }
        }
    }

    private fun initTabLayout() {
        binding.apply {
            tabLayout.onClickListener = {
                val iconType = if (it == 0) IconType.EXPENSE else IconType.INCOME
                setEvent(IconManagerEvent.IconTypeSelected(iconType))
            }
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
            recyclerView.adapter = iconAdapter
        }
    }

    private fun initAdapter() {
        iconAdapter = IconListAdapter(onEditClick = { model ->
            val directions =
                IconManagerFragmentDirections.actionIconManagerFragmentToCreateIconFragment(
                    isEdit = true,
                    icon = model.icon,
                    name = model.name,
                    iconType = model.type,
                    id = model.id
                )
            (activity as NavigationManager).navigateByDirection(directions)
        }, onDeleteClick = {
            showDialog(positiveButton = {
                setEvent(IconManagerEvent.DeleteIcon(it))
            })
        })
    }

    override fun renderState(state: IconManagerState) {
        state.icons?.let {
            iconAdapter.submitList(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as NavigationManager).bottomNavigationVisibility(true)
    }
}