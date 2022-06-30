package dev.imam.currencyconverter.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import dev.imam.currencyconverter.App
import dev.imam.currencyconverter.R
import dev.imam.currencyconverter.contract.HasCustomTitle
import dev.imam.currencyconverter.contract.statisticManager
import dev.imam.currencyconverter.model.Statistic
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val STATISTIC = "statistic"

class PersonalPageStatFragment : Fragment(), HasCustomTitle {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var spendTimeTextView: TextView
    private lateinit var convertCountTextView: TextView
    private lateinit var statistic: Statistic

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PersonalPageStatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        statistic = if(savedInstanceState != null){
            savedInstanceState.getParcelable(STATISTIC)!!
        } else {
            statisticManager().getStatistic()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_personal_page_stat, container, false)
        spendTimeTextView = view.findViewById(R.id.timeTextView)
        convertCountTextView  = view.findViewById(R.id.convertCountTextView)
        convertCountTextView.text = statistic.convertCount.toString()
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(STATISTIC, statistic)
    }

    override fun onResume() {
        super.onResume()
        (requireActivity().applicationContext as? App)?.apply {
            val myTimerTask = MyTimerTask()
            myTimerTask.startTime = startTime
            startTimer(myTimerTask)
        }
    }

    override fun onPause() {
        super.onPause()
        (requireActivity().applicationContext as? App)?.stopTimer()
    }

    override fun getTitleRes(): Int = R.string.statistic

    override fun getSelectedPageRes(): Int = 0

    inner class MyTimerTask() : TimerTask() {

        var startTime: Long = 0

        override fun run() {
            var allSeconds = (System.currentTimeMillis() - startTime) / 1000
            val hour = allSeconds / 3600
            val minute = (allSeconds / 60) % 60
            val second = allSeconds % 60

            requireActivity().runOnUiThread(Runnable
            {
                spendTimeTextView.text = "$hour ч. $minute мин. $second с."
            })
        }
    }
}