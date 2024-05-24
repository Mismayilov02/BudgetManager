package com.mismayilov.create

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mismayilov.common.utility.showDatePicker
import com.mismayilov.core.managers.NavigationManager
import com.mismayilov.create.databinding.FragmentCreateBinding

class CreateFragment : Fragment() {

    private var binding: FragmentCreateBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as NavigationManager).bottomNavigationVisibility(false)

        initClickListeners()
        initNumberPad()
        initTabLayout()


    }

    private fun initNumberPad() {
        binding!!.btn1.setOnClickListener {setAmount("1") }
        binding!!.btn2.setOnClickListener {setAmount("2") }
        binding!!.btn3.setOnClickListener {setAmount("3") }
        binding!!.btn4.setOnClickListener {setAmount("4") }
        binding!!.btn5.setOnClickListener {setAmount("5") }
        binding!!.btn6.setOnClickListener {setAmount("6") }
        binding!!.btn7.setOnClickListener {setAmount("7") }
        binding!!.btn8.setOnClickListener {setAmount("8") }
        binding!!.btn9.setOnClickListener {setAmount("9") }
        binding!!.btn0.setOnClickListener {setAmount("0") }
        binding!!.btnDot.setOnClickListener {setAmount(".") }
        binding!!.btnDelete.setOnClickListener {
            binding!!.txtAmount.text = binding!!.txtAmount.text.dropLast(1)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setAmount(text: String) {
        val amount = binding!!.txtAmount.text.toString()
        if (amount.length > 15) {
            Toast.makeText(requireContext(), "Amount is too long", Toast.LENGTH_SHORT).show()
            return
        }
        if (amount == "0" && text != ".") binding!!.txtAmount.text = text
        else if ((amount.contains(".") && text == ".") || (amount.isEmpty() && text == ".")) return
        else binding!!.txtAmount.text = "${binding!!.txtAmount.text}$text"
    }

    private fun initTabLayout() {
        binding!!.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                checkTabPosition(tab!!.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun checkTabPosition(position: Int) {
        when (position) {
            0 -> binding!!.btnFinish.text = "Expense Now"
            1 -> binding!!.btnFinish.text = "Income Now"
            2 -> binding!!.btnFinish.text = "Transfer Now"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initClickListeners() {
        binding!!.btnBack.setOnClickListener {
            (activity as NavigationManager).back()
        }

        binding!!.btnDate.setOnClickListener {
            showDatePicker(requireContext()) { day, month, year ->
                binding!!.txtDate.text = "$day $month\n$year"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}