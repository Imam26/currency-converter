package dev.imam.currencyconverter.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dev.imam.currencyconverter.R
import dev.imam.currencyconverter.contract.*
import dev.imam.currencyconverter.ui.DynamicStatePagerAdapter

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PersonalPageFragment : Fragment(), HasCustomTitle, HasCustomAction {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var personImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_personal_page, container, false)

        personImageView = view.findViewById(R.id.person_image_view)

        navigator().listenResult(Bitmap::class.java, viewLifecycleOwner) {
            personImageView.background = null
            personImageView.setImageBitmap(it)
        }

        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
        val adapter = DynamicStatePagerAdapter(requireActivity())
        adapter.setFragments(listOf(PersonalPageMainFragment(), PersonalPageStatFragment(), OnDevelopFragment()))
        viewPager.adapter = adapter
        viewPager.setCurrentItem(0, true)

        TabLayoutMediator(view.findViewById(R.id.tabLayout), viewPager) { tab, position ->
            tab.text = getString(adapter.getTitle(position))
        }.attach()

        return view
    }

    override fun getTitleRes(): Int = R.string.personal_page

    override fun getSelectedPageRes(): Int = R.id.page_5

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PersonalPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun getLayoutRes(): Int = R.menu.personal_page_menu

    override fun getCustomActionMap(): Map<ActionType, CustomAction<Any>> {
        return mapOf()
    }
}