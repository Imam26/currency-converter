package dev.imam.currencyconverter.presentation.view

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

interface ITouchListener {
    fun onSwipeAction(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>, viewHolder: RecyclerView.ViewHolder): Unit
    fun isAllowSwipe(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Boolean
    fun onMoveAction(recyclerView: RecyclerView, from: Int, to: Int): Boolean
}

class RecyclerViewWrapper: ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN,
    ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {

    private var recyclerView: RecyclerView? = null
    private lateinit var iTouchListener: ITouchListener

    fun setup(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
              layoutManager: RecyclerView.LayoutManager, itemAnimator: RecyclerView.ItemAnimator){
        this.recyclerView = recyclerView.apply {
            this.adapter = adapter
            this.layoutManager = layoutManager
            this.itemAnimator = itemAnimator
        }
    }

    fun setOnTouchListener(iTouchListener: ITouchListener){
        this.iTouchListener = iTouchListener
    }

    fun attachTouchHelperToRecyclerView(itemTouchHelper: ItemTouchHelper){
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition
        return iTouchListener.onMoveAction(recyclerView, from, to)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        recyclerView?.let{
            it.adapter?.apply {
                iTouchListener.onSwipeAction(this, viewHolder)
            }
        }
    }

    override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        if(!iTouchListener.isAllowSwipe(recyclerView, viewHolder)) return 0
        return super.getSwipeDirs(recyclerView, viewHolder)
    }
}
