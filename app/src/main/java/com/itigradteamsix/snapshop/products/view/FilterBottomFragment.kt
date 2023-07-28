package com.itigradteamsix.snapshop.products.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.databinding.FragmentFilterBottomBinding
import kotlin.math.min


class FilterBottomFragment : BottomSheetDialogFragment() {
    var maxPrice: Int = 0
    var minPrice: Int = 0
    var type: String? = null
    val isFilter = true
    var isPrice: Boolean = false
    var isType: Boolean = false
    private var filterOptionsListener: FilterOptionsListener? = null


    lateinit var binding: FragmentFilterBottomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFilterBottomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.typeRdbg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.shoes_rdb -> {
                    type = "SHOES"
                }

                R.id.accessories_rdb -> {
                    type = "ACCESSORIES"

                }

                R.id.t_rdb -> type = "T-SHIRTS"
            }
            isType = true
        }
        binding.button.setOnClickListener {


            if (binding.fromPrice.text.isNotEmpty() && binding.toPrice.text.isNotEmpty()) {
                if (binding.fromPrice.text.toString().toInt()
                    < binding.toPrice.text.toString().toInt()
                ) {

                    minPrice = binding.fromPrice.text.toString().toInt()
                    maxPrice = binding.toPrice.text.toString().toInt()
                    isPrice = true

                } else {
                    showMsgDialog()
                }

            }
            filterOptionsListener?.onFilterOptionsSelected(
                minPrice,
                maxPrice,
                type,
                isFilter,
                isPrice,
                isType
            )
            dismiss()
        }
    }

    fun setFilterOptionsListener(listener: FilterOptionsListener) {
        this.filterOptionsListener = listener
    }

    private fun showMsgDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val messageTextView = TextView(requireContext())

        messageTextView.text = "from value must be lees than to value"
        messageTextView.gravity = Gravity.CENTER

        val container = FrameLayout(requireContext())
        container.addView(messageTextView)

        builder.setView(container)
        builder.setCancelable(true)
        builder.setPositiveButton("Ok") { _: DialogInterface?, _ ->
            dismiss()
        }

        builder.show()
    }

}