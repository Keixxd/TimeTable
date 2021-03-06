package com.example.timetable.presentation.components

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.core.view.forEach
import androidx.core.view.get
import com.example.timetable.R
import com.example.timetable.databinding.DrawerLayoutBinding
import com.example.timetable.presentation.viewmodels.JobViewModel

/*
  *   MainActivityNavigationView class responsible for Navigation View behavior, and also
  *   view's buttons.
  *   the reason it is an inner class is that there is no need in passing through activity instance,
  *   making to write it more simpler and faster.
  *
  *   every time we add a new table, it also switch us to it, changing PagerAdapter in ViewPager, thereby
  *   opening new database with provided name.
  *
  */

class MainActivityNavigationView(val binding: DrawerLayoutBinding, val context: Context, val viewModel: JobViewModel) {

    companion object{
        private var itemCounter = 0
    }

    fun onLoadItemsInNavigationView(tablesList: List<String>?) {
        val menu = getTablesMenu()
        for (table in tablesList!!)
            menu.add(
                R.id.item_group,
                itemCounter++,
                Menu.NONE,
                table
            ).setOnMenuItemClickListener { menuItem ->
                onItemClick(menuItem)
                true
            }
        menu.get(0).setChecked(true)
    }

    fun onBuildAddItemDialog() {
        val builder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.add_job_table_dialog, null, false)
        builder.setTitle(context.getString(R.string.add_table_dialog_title))
            .setView(dialogView)
            .setCancelable(true)
            .setPositiveButton(R.string.ok, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    val editText = dialogView.findViewById<EditText>(R.id.editTextTableName)
                    if (!editText.text.toString().isEmpty()) {
                        val newItem = addItemInNavigationView(editText)
                        onItemClick(newItem)
                    }
                }
            })
            .show()
        binding.drawerLayout.closeDrawers()
    }

    private fun addItemInNavigationView(editText: EditText): MenuItem {
        val menu = getTablesMenu()
        return menu.add(
            R.id.item_group,
            itemCounter++,
            Menu.NONE,
            editText.text.toString()
        ).setOnMenuItemClickListener { menuItem ->
            onItemClick(menuItem)
            true
        }.setChecked(true)
    }

    private fun setCheckMenuButton(menu: Menu, menuItem: MenuItem) {
        menu.forEach {
            if (it.isChecked)
                it.setChecked(false)
            menuItem.setChecked(true)
        }
    }

    private fun switchToNewTable(menuItem: MenuItem) {
        viewModel.getDatabaseNameObservable().value = menuItem.title.toString()
        binding.main.listsPager.adapter?.notifyDataSetChanged()
    }

    private fun getTablesMenu(): Menu = binding.navView.menu.getItem(0).subMenu

    private fun onItemClick(menuItem: MenuItem) {
        switchToNewTable(menuItem)
        setCheckMenuButton(getTablesMenu(), menuItem)
    }

    fun setNextItemChecked(item: MenuItem?): MenuItem{
        var id = item?.itemId
        when(id){
            itemCounter -> return getTablesMenu().get(--id)
            else -> {
                id=+1
                return getTablesMenu().get(id)
            }
        }
    }

    fun removeItemFromMenu(): MenuItem?{
        val item = findCheckedItem()
        item?.setVisible(false)
        return item
    }

    private fun findCheckedItem(): MenuItem?{
        var findResult: MenuItem? = null
        getTablesMenu().forEach { item ->
            if(item.isChecked){
                item.setChecked(false)
                findResult = item
            }
        }
        return findResult
    }

    fun updateNavViewMenu(result: String?) {
        with(binding){
            navView.menu[0].subMenu.forEach {
                if(it.title.equals(viewModel.getDatabaseNameObservable().value))
                    it.title = result
            }
        }
    }
}