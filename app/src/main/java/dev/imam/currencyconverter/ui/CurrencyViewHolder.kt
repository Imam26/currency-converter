package dev.imam.currencyconverter.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dev.imam.currencyconverter.R
import dev.imam.currencyconverter.helper.CurrencyPayload
import dev.imam.currencyconverter.model.Currency
import kotlin.coroutines.suspendCoroutine

class CurrencyViewHolder(
    inflater: LayoutInflater,
    parent: ViewGroup,
): RecyclerView.ViewHolder(inflater.inflate(R.layout.item_currency, parent, false)) {

    private val currencyTextView = itemView.findViewById<TextInputEditText>(R.id.currencyTextField)
    private val currencyTextLayout = itemView.findViewById<TextInputLayout>(R.id.currencyTextLayout)
    private val currencyLabel = itemView.findViewById<TextView>(R.id.currencyLabel)
    private val currencyImage = itemView.findViewById<ImageView>(R.id.currencyImage)
    private var textChangedListener:TextWatcher? = null

    fun onBind(item:Currency, onTextChange:(text:String) -> Unit) {
        detachEventListener()
        currencyTextView.text?.clear()
        currencyTextView.text?.append(item.amount.toString())
        currencyTextLayout.hint = item.fullName
        currencyLabel.text = item.fullName
        currencyImage.setImageResource(item.imageRes)
        currencyTextView.isEnabled = item.isMain

        if(item.isMain){
            attachEventListener(onTextChange)
        }
    }

    fun onBind(item:Currency, fields: Set<*>?){
        fields?.forEach {
            when (it) {
                CurrencyPayload.AMOUNT -> {
                    currencyTextView.text?.clear()
                    currencyTextView.text?.append(item.amount.toString())
                }
            }
        }
    }

    private fun attachEventListener(onTextChange:(text:String) -> Unit){
        var count = 0
        textChangedListener = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                return
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                return
            }

            override fun afterTextChanged(p0: Editable?) {
                if(count == 0) onTextChange(p0.toString())
                if(count == 2) count = 0
                else count++
            }

        }
        currencyTextView.addTextChangedListener(textChangedListener)
    }

    private fun detachEventListener(){
        if(textChangedListener == null) return
        currencyTextView.removeTextChangedListener(textChangedListener)
    }
}