package com.mismayilov.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.mismayilov.common.unums.TransactionType
import com.mismayilov.core.managers.NavigationManager
import com.mismayilov.domain.entities.local.CategoryModel
import com.mismayilov.domain.entities.local.TransactionModel
import com.mismayilov.home.adapter.RecentTransactionAdapter
import com.mismayilov.home.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private val adapter = RecentTransactionAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? NavigationManager)?.bottomNavigationVisibility(true)

        initClickListeners()

        val category = CategoryModel("Salary", "salary", TransactionType.INCOME, 1)
        val category2 = CategoryModel("Salary", "education", TransactionType.EXPENSE, 1)
        val category3 = CategoryModel("Salary", "tik_tok", TransactionType.INCOME, 1)
        val category4 = CategoryModel("Salary", "health", TransactionType.EXPENSE, 1)
        val category5 = CategoryModel("Salary", "part_time", TransactionType.INCOME, 1)

        val data = listOf(
            TransactionModel(
                1.0,
                1.0,
                null,
                56516552451,
                category,
                "Salary"),
            TransactionModel(
                1.0,
                1.0,
                null,
                56516552451,
                category2,
                "Salary"),
            TransactionModel(
                1.0,
                1.0,
                null,
                56516552451,
                category3,
                "Salary"),
            TransactionModel(
                1.0,
                1.0,
                null,
                56516552451,
                category4,
                "Salary"),
            TransactionModel(
                1.0,
                1.0,
                null,
                56516552451,
                category5,
                "Salary"),
            TransactionModel(
                1.0,
                1.0,
                null,
                56516552451,
                category2,
                "Salary"),
        )
        binding?.apply {
            transactionHistoryRecyclerView.setHasFixedSize(true)
            transactionHistoryRecyclerView.layoutManager =GridLayoutManager(requireContext(), 1)
            transactionHistoryRecyclerView.adapter = adapter
        }
        adapter.submitList(data)
    }

    private fun initClickListeners() {
        binding!!.btnIncome.setOnClickListener {
            (activity as NavigationManager).navigateByBottomNavigation("create_navigation")
        }
        binding!!.btnExpense.setOnClickListener {
            (activity as NavigationManager).navigateByBottomNavigation("create_navigation")
        }
        binding!!.btnTransfer.setOnClickListener {
            (activity as NavigationManager).navigateByBottomNavigation("create_navigation")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}