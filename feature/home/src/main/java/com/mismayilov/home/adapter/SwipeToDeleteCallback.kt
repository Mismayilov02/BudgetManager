package com.mismayilov.home.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.mismayilov.home.R

class SwipeToDeleteCallback(val context: Context,private val adapter: RecentTransactionAdapter) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false // Sürükleme işlemini desteklemiyoruz, bu yüzden false döndürüyoruz
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Öğeyi adapter'dan sil
        val position = viewHolder.adapterPosition
        adapter.deleteItem(position)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val background = ColorDrawable(Color.RED)
        val deleteIcon = ContextCompat.getDrawable(context, com.mismayilov.uikit.R.drawable.trash)!!
        val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2

        if (dX < 0) { // Öğeyi sola kaydırırken
            background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
            deleteIcon.setBounds(
                itemView.right - iconMargin - deleteIcon.intrinsicWidth,
                itemView.top + iconMargin,
                itemView.right - iconMargin,
                itemView.bottom - iconMargin
            )
        } else {
            background.setBounds(0, 0, 0, 0) // Arkaplanı sıfırla
            deleteIcon.setBounds(0, 0, 0, 0) // İkonu sıfırla
        }

        background.draw(c)
        deleteIcon.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}