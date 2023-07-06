package com.nexlesoft.ket.ui.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.nexlesoft.ket.R
import com.nexlesoft.ket.data.model.CategoryItem

internal class CategoryListAdapter internal constructor(
    context: Context,
    private val resource: Int,
    private val itemList: List<CategoryItem>
) : ArrayAdapter<CategoryListAdapter.ItemViewHolder>(context, resource) {

    private val selectedItems: ArrayList<CategoryItem> = arrayListOf<CategoryItem>()

    private fun addItem(item: CategoryItem) {
        selectedItems.add(item)
        notifyDataSetChanged()
    }

    private fun removeItem(item: CategoryItem) {
        selectedItems.removeIf {
            it.id == item.id
        }
        notifyDataSetChanged()
    }

    private fun hasSelected(item: CategoryItem): Boolean {
        for (i in selectedItems) {
            if (item.id == i.id) {
                return true
            }
        }
        return false
    }

    fun hasSelectedItem(): Boolean {
        return selectedItems.isNotEmpty()
    }

    fun getSelectedItems(): List<CategoryItem> {
        return selectedItems.toList()
    }

    override fun getCount(): Int {
        return this.itemList.size
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var convertView = view
        val holder: ItemViewHolder
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(context)
            convertView = layoutInflater.inflate(resource, parent, false)
            holder = ItemViewHolder()
            holder.categoryName = convertView!!.findViewById(R.id.txt_category_name)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ItemViewHolder
        }
        val currentItem = this.itemList[position]
        holder.categoryName?.text = currentItem.name
        holder.categoryName?.setOnClickListener {
            if (hasSelected(currentItem)) {
                removeItem(currentItem)
            } else {
                addItem(currentItem)
            }
        }
        if (hasSelected(currentItem)) {
            holder.categoryName?.setBackgroundResource(R.drawable.category_selected_background)
        } else {
            holder.categoryName?.setBackgroundResource(R.drawable.category_unselected_background)
        }
        return convertView
    }

    internal class ItemViewHolder {
        var categoryName: AppCompatTextView? = null
    }
}
