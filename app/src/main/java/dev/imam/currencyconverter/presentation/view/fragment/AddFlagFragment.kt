package dev.imam.currencyconverter.presentation.view.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.imam.currencyconverter.R
import dev.imam.currencyconverter.presentation.contract.navigator
import dev.imam.currencyconverter.presentation.model.CurrencyService
import dev.imam.currencyconverter.presentation.view.adapter.FlagAdapter
import dev.imam.currencyconverter.presentation.view.RecyclerViewItemDecorator

class AddFlagFragment : BottomSheetDialogFragment() {

    private lateinit var currencyService: CurrencyService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currencyService = CurrencyService(
            activity?.getSharedPreferences(
                "test",
                Context.MODE_PRIVATE
            )
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.select_flag_modal, container, false)
        setup(view)
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { dialogInterface ->
            val dialogView = dialogInterface as BottomSheetDialog
            val bottomSheet =
                dialogView.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as? FrameLayout?
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(bottomSheet)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.isDraggable = false
                isCancelable = true
            }
        }
        return dialog
    }

    private fun setup(view: View) {
        val flagAdapter = FlagAdapter {
            navigator().publishResult(it)
        }
        flagAdapter.setItems(currencyService.getFlagList())

        val flagLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.flagRecyclerView).apply {
            adapter = flagAdapter
            layoutManager = flagLayoutManager
            itemAnimator = DefaultItemAnimator()
        }

        val itemDecoration = RecyclerViewItemDecorator(12, 20)
        recyclerView.addItemDecoration(itemDecoration)
    }
}