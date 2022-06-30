package dev.imam.currencyconverter.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dev.imam.currencyconverter.R
import dev.imam.currencyconverter.presentation.contract.*
import dev.imam.currencyconverter.presentation.model.CurrencyModel
import dev.imam.currencyconverter.presentation.view.ITouchListener
import dev.imam.currencyconverter.presentation.view.RecyclerViewWrapper
import dev.imam.currencyconverter.presentation.view.activity.MenuType
import dev.imam.currencyconverter.presentation.viewmodel.CurrencyViewModel
import dev.imam.currencyconverter.presentation.viewmodel.SortType
import dev.imam.currencyconverter.presentation.view.viewholder.AddViewHolder
import dev.imam.currencyconverter.presentation.view.adapter.CurrencyAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrencyFragment : Fragment(), CurrencyView, HasCustomTitle, HasCustomAction {
    lateinit var currencyAdapter: CurrencyAdapter
    val viewModel: CurrencyViewModel by viewModel()
    private lateinit var currencyLayoutManager: LinearLayoutManager
    private lateinit var coordinatorView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currencyAdapter = createCurrencyAdapter()

        viewModel.currencyModelListLiveData.observe(this) {
            reloadData(it.toMutableList())
        }

        viewModel.addCurrencyResultLiveData.observe(this) {
            closeAddCurrencyBottomSheet()
        }

        viewModel.preparedToDeleteCurrencyName.observe(this) {
            changeToolBarState(MenuType.DELETE, getToolBarTitle(MenuType.DELETE, it))
        }

        viewModel.deleteCurrencyResultLiveData.observe(this) {
            changeToolBarState(MenuType.DEFAULT, getToolBarTitle(MenuType.DEFAULT))
            showShackBar()
        }

        viewModel.loadCurrencyModelList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_currency, container, false)
        coordinatorView = view.findViewById(R.id.coordinator)
        setup(view)
        navigator().listenResult(CurrencyModel::class.java, viewLifecycleOwner) {
            viewModel.addCurrency(it)
        }
        navigator().listenResult(ConfirmDialogResult::class.java, viewLifecycleOwner) {
            if (it.result == ConfirmDialogResult.Result.OK) {
                deleteCurrencyFromUI()
            } else if (it.result == ConfirmDialogResult.Result.CANCEL) {
                navigator().closeConfirmDialog()
                viewModel.resetPreparedToDeleteCurrency()
            }
        }
        return view
    }

    override fun reloadData(data: MutableList<CurrencyModel>) {
        if (currencyAdapter.itemCount == 0) currencyAdapter.setItems(ArrayList(data.map { it.copy() }))
        else currencyAdapter.setItemsWithDiffCallback(ArrayList(data.map { it.copy() }))
    }

    override fun closeAddCurrencyBottomSheet() {
        navigator().closeAddCurrencyBottomSheet()
        scrollToTop()
    }

    override fun changeToolBarState(menuType: MenuType, title: String) {
        navigator().closeConfirmDialog()
        (activity as? ToolBarState)?.changeToolBarState(menuType, title)
    }

    override fun getToolBarTitle(menuType: MenuType, title: String): String {
        return when (menuType) {
            MenuType.DEFAULT -> getString(R.string.converter)
            MenuType.DELETE -> getString(R.string.delete_toolbar_title, title)
        }
    }

    override fun showShackBar() {
        val undoSnackBar =
            Snackbar.make(coordinatorView, R.string.currency_deleted, Snackbar.LENGTH_SHORT)
        undoSnackBar.setAction(R.string.undo_string) {
            viewModel.undoDeleteCurrency()
        }
        undoSnackBar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                if (event == DISMISS_EVENT_TIMEOUT) {
                    viewModel.deleteCurrencyFromDB()
                }
            }
        })
        undoSnackBar.show()
    }

    private fun createCurrencyAdapter(): CurrencyAdapter{
        return CurrencyAdapter(
            clickListener = {
                navigator().showAddCurrencyBottomSheet()
            },
            onLongClickListener = { item, position ->
                (item as? CurrencyModel)?.let {
                    viewModel.prepareCurrencyToDelete(it, position)
                }
            },
            onTextChange = {
                if (it.toIntOrNull() != null){
                    statisticManager().increaseConvertCount()
                    viewModel.updateMainCurrencyAmount(it.toInt())
                }
            }
        )
    }

    private fun showConfirmDialog() {
        navigator().showConfirmDialog()
    }

    private fun setup(view: View) {
        val recyclerViewWrapper = RecyclerViewWrapper()
        val itemTouchHelper = ItemTouchHelper(recyclerViewWrapper)

        currencyLayoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        recyclerViewWrapper.setup(
            view.findViewById(R.id.currencyRecyclerView),
            currencyAdapter,
            currencyLayoutManager,
            DefaultItemAnimator()
        )

        recyclerViewWrapper.setOnTouchListener(object : ITouchListener {
            override fun onSwipeAction(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>, viewHolder: RecyclerView.ViewHolder) {
                if (adapter is CurrencyAdapter) {
                    val removed = adapter.getItemByPosition(viewHolder.adapterPosition) as? CurrencyModel
                    removed?.let {
                        viewModel.deleteSwipedCurrency(it, viewHolder.adapterPosition)
                    }
                }
            }

            override fun isAllowSwipe(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Boolean {
                val adapter = recyclerView.adapter
                if (adapter is CurrencyAdapter) {
                    val swiped = adapter.getItemByPosition(viewHolder.adapterPosition) as? CurrencyModel
                    swiped?.let {
                        return !it.isMain
                    }
                }
                return viewHolder !is AddViewHolder
            }

            override fun onMoveAction(recyclerView: RecyclerView, from: Int, to: Int): Boolean {
                currencyAdapter.moveItem(from, to)
                return true
            }
        })

        recyclerViewWrapper.attachTouchHelperToRecyclerView(itemTouchHelper)
    }

    private fun deleteCurrencyFromUI() {
        viewModel.deleteCurrencyFromUI()
    }

    private fun scrollToTop() {
        val smoothScroller = object : LinearSmoothScroller(this.context) {
            override fun getVerticalSnapPreference(): Int =
                SNAP_TO_START
        }
        smoothScroller.targetPosition = 0
        currencyLayoutManager.startSmoothScroll(smoothScroller)
    }

    override fun getTitleRes(): Int = R.string.converter

    override fun getSelectedPageRes(): Int = R.id.page_3

    override fun getLayoutRes(): Int = R.menu.main_menu

    override fun getCustomActionMap(): Map<ActionType, CustomAction<Any>> {
        val sort: (Any) -> Unit = { sortType ->
            viewModel.sortType = sortType as SortType
            viewModel.sortCurrencyList()
        }
        return mapOf(
            ActionType.SORT_BY_ALPHA to CustomAction {
                it?.let(sort)
            },
            ActionType.SORT_BY_AMOUNT to CustomAction {
                it?.let(sort)
            },
            ActionType.SORT_DEFAULT to CustomAction {
                it?.let(sort)
            },
            ActionType.DELETE to CustomAction {
                showConfirmDialog()
            }
        )
    }

}

interface ToolBarState {
    fun changeToolBarState(menuType: MenuType, title: String)
}