package dev.imam.currencyconverter.presentation.view.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dev.imam.currencyconverter.R
import dev.imam.currencyconverter.presentation.contract.HasCustomTitle
import dev.imam.currencyconverter.presentation.contract.navigator

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PersonalPageMainFragment : Fragment(), HasCustomTitle {
    private var param1: String? = null
    private var param2: String? = null

    private val resultActivityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                it.data?.let { intent ->
                    val imageBitmap = intent.extras?.get("data") as Bitmap
                    navigator().publishResult(imageBitmap)
                }
            }
        }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PersonalPageMainFragment().apply {
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_personal_page_main, container, false)

        view.findViewById<Button>(R.id.shareBtn).setOnClickListener {
            dispatchShareIntent(view)
        }

        view.findViewById<Button>(R.id.callBtn).setOnClickListener {
            dispatchCallIntent()
        }

        view.findViewById<Button>(R.id.postSendBtn).setOnClickListener {
            dispatchSendPostIntent()
        }

        view.findViewById<Button>(R.id.launchCameraBtn).setOnClickListener() {
            dispatchTakePictureIntent()
        }

        return view
    }

    override fun getTitleRes(): Int = R.string.main_title

    override fun getSelectedPageRes(): Int = 0

    private fun dispatchShareIntent(view: View) {
        val personsMail = view.findViewById<TextView>(R.id.mailTextView)?.text
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, personsMail)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, personsMail))
    }

    private fun dispatchCallIntent() {
        val callIntent = Intent(Intent.ACTION_DIAL)
        startActivity(callIntent)
    }

    private fun dispatchSendPostIntent() {
        val sendPostIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, getString(R.string.type_message))
            type = "message/rfc822"
        }
        startActivity(Intent.createChooser(sendPostIntent, getString(R.string.choose_post_app)))
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            resultActivityResult.launch(takePictureIntent)
        } catch (e: ActivityNotFoundException) {
            showToast(getString(R.string.cam_not_found))
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}