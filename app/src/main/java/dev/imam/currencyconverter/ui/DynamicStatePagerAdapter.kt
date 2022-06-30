package dev.imam.currencyconverter.ui

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.imam.currencyconverter.R
import dev.imam.currencyconverter.contract.HasCustomTitle

class DynamicStatePagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {
    private var fragmentsHolder = listOf<FragmentsHolder>()

    override fun createFragment(position: Int): Fragment = fragmentsHolder[position].fragment

    override fun containsItem(itemId: Long): Boolean =
        fragmentsHolder.firstOrNull { it.id.toLong() == itemId } != null

    override fun getItemId(position: Int): Long = fragmentsHolder[position].id.toLong()

    override fun getItemCount(): Int = fragmentsHolder.size

    fun setFragments(fragments: List<Fragment>) {
        fragmentsHolder = fragments.mapIndexed{ index, fragment ->
            FragmentsHolder(fragment, index)
        }
        notifyDataSetChanged()
    }

    @StringRes
    fun getTitle(position: Int): Int{
        return (fragmentsHolder[position].fragment as? HasCustomTitle)?.getTitleRes() ?: R.string.not_title
    }

    private data class FragmentsHolder(val fragment: Fragment, val id: Int)
}