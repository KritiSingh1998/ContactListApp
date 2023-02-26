package com.example.contactlistapp.ui

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactlistapp.R
import com.example.contactlistapp.databinding.ContactListActivityBinding
import com.example.contactlistapp.model.ContactInfo
import com.example.contactlistapp.ui.adapter.ContactListAdapter
import com.example.contactlistapp.util.AppConstant.Companion.CONTACT_ID
import com.example.contactlistapp.util.AppConstant.Companion.DELETED_CONTACT_ID
import com.example.contactlistapp.util.AppConstant.Companion.IS_CONTACT_DELETED
import com.example.contactlistapp.viewmodel.ContactListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ContactListActivity : AppCompatActivity() {
    private lateinit var binding: ContactListActivityBinding
    private val viewModel: ContactListViewModel by viewModels()
    private lateinit var contactListAdapter: ContactListAdapter
    private lateinit var contactList: List<ContactInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContactListActivityBinding.inflate(layoutInflater)
        supportActionBar?.title = getString(R.string.contacts)
        viewModel.getAllContacts()
        setAdapter()
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.contact_sort_menu, menu)
        val item: MenuItem = menu!!.findItem(R.id.search)
        val searchView: SearchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                filter(msg)
                return false
            }
        })
        return true
    }


    private fun filter(text: String) {
        val filteredlist: ArrayList<ContactInfo> = ArrayList()

        for (item in contactList) {
            if (item.name.toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            contactListAdapter.filterList(filteredlist)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val optionId = item.itemId
        var sortedList: List<ContactInfo>
        if (optionId == R.id.sort_ascending) {
            sortedList = contactListAdapter.getCurrentList().sortedBy { it.name }
        } else sortedList = contactListAdapter.getCurrentList().sortedByDescending { it.name }
        contactListAdapter.updateList(sortedList)
        return true
    }

    private fun setAdapter() {
        val recyclerView = binding.recyclerView
        contactListAdapter =
            ContactListAdapter(listOf(), onContactCardLongClickBehavior, onContactCardClickBehavior)
        recyclerView.adapter = contactListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        this.let {
            ContextCompat.getDrawable(it, R.drawable.contact_list_divider)?.let { drawable ->
                divider.setDrawable(drawable)
            }
        }
        recyclerView.addItemDecoration(divider)
        recyclerView.itemAnimator = null
        subscribeObserver()
    }

    private fun subscribeObserver() {
        viewModel.progress.observe(
            this
        ) {
            if (it) {
                binding.progress?.visibility = View.VISIBLE
            } else {
                binding.progress?.visibility = View.GONE
            }
        }

        viewModel.contactListData.observe(
            this
        ) { contactListData ->
            contactList = contactListData
            contactListAdapter.updateList(contactList)
        }
    }

    private val onContactCardLongClickBehavior: (id: Int) -> Unit = { id ->
        val builder = AlertDialog.Builder(this).setTitle(getString(R.string.delete_contact))
            .setMessage(getString(R.string.delete_cofirmation))
            .setPositiveButton(
                getString(R.string.yes),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    contactListAdapter.removeContact(id)
                }).setNegativeButton(
                getString(R.string.no),
                DialogInterface.OnClickListener { dialogInterface, i ->

                })

        builder.show()
    }

    private val onContactCardClickBehavior: () -> Unit = {
        val selectedContactId = contactListAdapter.selectedContactId
        val intent = Intent(this, ContactDetailsActivity::class.java)
        intent.putExtra(CONTACT_ID, selectedContactId)
        startForDetailsResult.launch(intent)
    }


    private val startForDetailsResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                intent?.let {
                    val deletedId = it.getIntExtra(DELETED_CONTACT_ID, 0)
                    contactListAdapter.removeContact(deletedId)
                }
            }
        }
}
