package com.mismayilov.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.mismayilov.domain.entities.local.TransactionCategory
import com.mismayilov.domain.entities.local.TransactionHistory
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
        val category = TransactionCategory("Salary", "send_money", "income", 1)
        val category2 = TransactionCategory("Salary", "send_money", "ressd", 1)
        val data = listOf(
            TransactionHistory(
                1.0,
                1.0,
                null,
                56516552451,
                category,
                "Salary"),
            TransactionHistory(
                1.0,
                1.0,
                null,
                56516552451,
                category2,
                "Salary"),
            TransactionHistory(
                1.0,
                1.0,
                null,
                56516552451,
                category,
                "Salary"),
            TransactionHistory(
                1.0,
                1.0,
                null,
                56516552451,
                category2,
                "Salary"),
            TransactionHistory(
                1.0,
                1.0,
                null,
                56516552451,
                category,
                "Salary"),
            TransactionHistory(
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}