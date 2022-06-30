package dev.imam.currencyconverter.presentation.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.navigation.NavigationBarView
import dev.imam.currencyconverter.R
import dev.imam.currencyconverter.presentation.contract.*
import dev.imam.currencyconverter.presentation.model.Statistic
import dev.imam.currencyconverter.presentation.model.StatisticService
import dev.imam.currencyconverter.presentation.view.fragment.*
import dev.imam.currencyconverter.presentation.viewmodel.MainPresenter
import dev.imam.currencyconverter.presentation.viewmodel.SortType

enum class MenuType {
    DEFAULT, DELETE
}

private const val CURRENT_PAGE = "CurrentPage"

class MainActivity: AppCompatActivity(), ToolBarState, Navigator, StatisticManager, MainView {
    private lateinit var toolBar: Toolbar
    private lateinit var bottomNavigationView: NavigationBarView

    private var menuType: MenuType = MenuType.DEFAULT

    private val currencyFragment: CurrencyFragment = CurrencyFragment()
    private val confirmDialog: ConfirmDeleteFragment = ConfirmDeleteFragment()

    private val currentFragment: Fragment
        get() = supportFragmentManager.findFragmentById(R.id.fragmentContainer)!!

    private val fragmentListener = object: FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            updateUI()
        }
    }

    private var customActions: Map<ActionType, CustomAction<Any>> = mapOf()

    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolBar = findViewById(R.id.toolBar)
        setSupportActionBar(toolBar)
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)

        bottomNavigationView = findViewById(R.id.navigationMenu)
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.page_1 -> {
                    replaceFragment(HistoryFragment())
                    true
                }
                R.id.page_2 -> {
                    replaceFragment(FavoriteFragment())
                    true
                }
                R.id.page_3 -> {
                    replaceFragment(currencyFragment)
                    true
                }
                R.id.page_4 -> {
                    replaceFragment(SearchFragment())
                    true
                }
                R.id.page_5 -> {
                    replaceFragment(PersonalPageFragment())
                    true
                }
                else -> false
            }
        }

        if(savedInstanceState != null){
            bottomNavigationView.selectedItemId = savedInstanceState.getInt(CURRENT_PAGE)
        } else {
            addFragment(currencyFragment)
        }

        val statisticService =
            StatisticService(getSharedPreferences("test", MODE_PRIVATE))
        presenter = MainPresenter(statisticService)
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.updateStatistic()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CURRENT_PAGE, bottomNavigationView.selectedItemId)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        updateUI()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.sortByAlphaMenuItem -> {
                item.isChecked = !item.isChecked
                customActions[ActionType.SORT_BY_ALPHA]?.onCustomAction?.invoke(SortType.BY_ALPHA)
            }
            R.id.sortByCostMenuItem -> {
                item.isChecked = !item.isChecked
                customActions[ActionType.SORT_BY_AMOUNT]?.onCustomAction?.invoke(SortType.BY_AMOUNT)
            }
            R.id.resetSortMenuItem -> {
                invalidateOptionsMenu()
                customActions[ActionType.SORT_DEFAULT]?.onCustomAction?.invoke(SortType.DEFAULT)
            }
            R.id.deleteMenuItem -> {
                customActions[ActionType.DELETE]?.onCustomAction?.invoke(null)
            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onBackPressed() {
        if(menuType == MenuType.DEFAULT) {
            super.onBackPressed()
        } else {
            changeToolBarState(MenuType.DEFAULT, getString(R.string.converter))
        }
    }

    override fun <T : Parcelable> publishResult(result: T) {
        supportFragmentManager.setFragmentResult(result.javaClass.name, bundleOf(KEY_RESULT to result))
    }

    override fun <T : Parcelable> listenResult(clazz: Class<T>, owner: LifecycleOwner, listener: (T) -> Unit) {
        supportFragmentManager.setFragmentResultListener(clazz.name, owner) { _, bundle ->
            listener.invoke(bundle.getParcelable(KEY_RESULT)!!)
        }
    }

    override fun showAddCurrencyBottomSheet() {
        showFragment(AddCurrencyFragment(), "addCurrencyFragment", "parentStack")
    }

    override fun closeAddCurrencyBottomSheet() {
        supportFragmentManager.popBackStack("parentStack", 1)
    }

    override fun showAddFlagBottomSheet() {
        showFragment(AddFlagFragment(), "addFlagFragment", "childStack")
    }

    override fun closeAddFlagBottomSheet() {
        supportFragmentManager.popBackStack("childStack", 1)
    }

    override fun showConfirmDialog() {
        confirmDialog.show(supportFragmentManager, null)
    }

    override fun closeConfirmDialog() {
        if(confirmDialog.isVisible) confirmDialog.dismiss()
    }

    override fun changeToolBarState(menuType: MenuType, title: String) {
        toolBar.menu.clear()
        val inflater:MenuInflater = menuInflater
        toolBar.title = title
        if(menuType == MenuType.DELETE){
            toolBar.setBackgroundColor(getColor(R.color.grey))
            inflater.inflate(R.menu.delete_item_menu, toolBar.menu)
        } else {
            toolBar.setBackgroundColor(getColor(R.color.white))
            inflater.inflate(R.menu.main_menu, toolBar.menu)
        }
        this.menuType = menuType
    }

    override fun increaseConvertCount() = presenter.increaseConvertCount()

    override fun getStatistic(): Statistic = presenter.getStatistic()

    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        if(supportFragmentManager.findFragmentById(R.id.fragmentContainer)?.javaClass == fragment.javaClass) return

        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)

        if(addToBackStack)
            transaction.addToBackStack(null)

        transaction.commit()
    }

    private fun addFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val transaction = supportFragmentManager
            .beginTransaction()
            .add(R.id.fragmentContainer, fragment)

        if(addToBackStack)
            transaction.addToBackStack(null)

        transaction.commit()
    }

    private fun showFragment(fragment: Fragment, tag: String, backStackName: String = "") {
        supportFragmentManager
            .beginTransaction()
            .add(fragment, tag)
            .addToBackStack(backStackName)
            .commit()
    }

    private fun updateUI() {
        val fragment = currentFragment

        if(fragment is HasCustomTitle){
            toolBar.title = getString(fragment.getTitleRes())
            bottomNavigationView.selectedItemId = fragment.getSelectedPageRes()
        } else {
            toolBar.title = getString(R.string.converter)
        }

        toolBar.setBackgroundColor(getColor(R.color.white))
        toolBar.menu.clear()

        if(fragment is HasCustomAction){
            createCustomToolbarAction(fragment)
        }
    }

    private fun createCustomToolbarAction(customAction: HasCustomAction) {
        val inflater:MenuInflater = menuInflater
        inflater.inflate(customAction.getLayoutRes(), toolBar.menu)
        customActions = customAction.getCustomActionMap()
    }

    companion object {
        @JvmStatic private val KEY_RESULT = "RESULT"
    }
}