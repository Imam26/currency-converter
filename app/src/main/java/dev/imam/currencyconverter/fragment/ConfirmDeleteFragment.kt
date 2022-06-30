package dev.imam.currencyconverter.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Button
import androidx.fragment.app.DialogFragment
import dev.imam.currencyconverter.R
import dev.imam.currencyconverter.contract.navigator
import kotlinx.parcelize.Parcelize

@Parcelize
class ConfirmDialogResult(val result: Result) : Parcelable {
    enum class Result {
        OK,
        CANCEL
    }
}

class ConfirmDeleteFragment : DialogFragment(R.layout.confirm_delete_dialog) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customizeDialog()

        view.findViewById<Button>(R.id.cancelItemButton).setOnClickListener {
            navigator().publishResult(ConfirmDialogResult(ConfirmDialogResult.Result.CANCEL))
        }

        view.findViewById<Button>(R.id.deleteItemButton).setOnClickListener {
            navigator().publishResult(ConfirmDialogResult(ConfirmDialogResult.Result.OK))
        }
    }

    private fun customizeDialog() {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_confirm_delete_dialog)
    }
}