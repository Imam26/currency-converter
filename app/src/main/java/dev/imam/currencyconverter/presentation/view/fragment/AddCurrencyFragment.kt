package dev.imam.currencyconverter.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.annotation.DrawableRes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.imam.currencyconverter.R
import dev.imam.currencyconverter.presentation.contract.navigator
import dev.imam.currencyconverter.presentation.model.CurrencyModel
import dev.imam.currencyconverter.presentation.model.Flag

class AddCurrencyFragment : BottomSheetDialogFragment() {
    private lateinit var editTextCurrencyName: EditText
    private lateinit var editTextCurrencyAmount: EditText

    @DrawableRes
    var imageRes: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        navigator().listenResult(Flag::class.java, viewLifecycleOwner) {
            setCurrentCurrency(it)
            navigator().closeAddFlagBottomSheet()
        }
        return inflater.inflate(R.layout.add_currency_modal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        editTextCurrencyName = view.findViewById(R.id.editTextCurrencyName)
        editTextCurrencyAmount = view.findViewById(R.id.editTextCurrencyAmount)

        view.findViewById<Button>(R.id.buttonAdd).setOnClickListener {
            if (!validate()) return@setOnClickListener

            val currencyModel = CurrencyModel(
                id = 0,
                fullName = editTextCurrencyName.text.toString(),
                amount = 0,
                exchangeRate = editTextCurrencyAmount.text.toString().toInt(),
                imageRes = imageRes
            )
            navigator().publishResult(currencyModel)
        }

        view.findViewById<Button>(R.id.buttonSelectFlag).setOnClickListener {
            navigator().showAddFlagBottomSheet()
        }
    }

    private fun setCurrentCurrency(flag: Flag) {
        editTextCurrencyName.text.clear()
        editTextCurrencyName.text.append(flag.fullName)
        imageRes = flag.imageRes
    }

    private fun validate(): Boolean {
        var isValid = true
        if (editTextCurrencyName.text.isEmpty()) {
            editTextCurrencyName.error = "Required field"
            isValid = false
        }
        if (editTextCurrencyAmount.text.isEmpty()) {
            editTextCurrencyAmount.error = "Required field"
            isValid = false
        }
        if (editTextCurrencyAmount.text.toString().toIntOrNull() == null) {
            editTextCurrencyAmount.error = "Must be a number"
            isValid = false
        }
        return isValid
    }
}