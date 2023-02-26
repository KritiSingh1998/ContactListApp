package com.example.contactlistapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.contactlistapp.R
import com.example.contactlistapp.databinding.ContactDetailsActivityBinding
import com.example.contactlistapp.databinding.ContactListActivityBinding
import com.example.contactlistapp.model.ContactInfo
import com.example.contactlistapp.util.AppConstant.Companion.CONTACT_ID
import com.example.contactlistapp.util.AppConstant.Companion.DELETED_CONTACT_ID
import com.example.contactlistapp.util.AppConstant.Companion.IS_CONTACT_DELETED
import com.example.contactlistapp.viewmodel.ContactDetailsViewModel
import com.example.contactlistapp.viewmodel.ContactListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ContactDetailsActivityBinding
    private val viewModel: ContactDetailsViewModel by viewModels()
    private lateinit var contactDetails: ContactInfo
    private var contactId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContactDetailsActivityBinding.inflate(layoutInflater)
        contactId = intent.getIntExtra(CONTACT_ID, 1)
        viewModel.getContactDetails(contactId)
        subscribeObserver()
        setContentView(binding.root)
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

        viewModel.contactDetailsData.observe(
            this
        ) { contactDetailsData ->
            contactDetails = contactDetailsData
            setupScreen()
        }
    }

    private fun setupScreen() {
        setActionBar()
        binding.userName.details.text = contactDetails.username
        binding.userName.description.text = getString(R.string.username)
        binding.phone.details.text = contactDetails.phone
        binding.phone.description.text = getString(R.string.phone)
        binding.address.details.text = contactDetails.address.getCompleteAddress()
        binding.address.description.text = getString(R.string.address)
        binding.website.details.text = contactDetails.website
        binding.website.description.text = getString(R.string.website)
        binding.company.details.text = contactDetails.company.getCompleteCompany()
        binding.company.description.text = getString(R.string.company)
        binding.email.details.text = contactDetails.email
        binding.email.description.text = getString(R.string.email)
    }

    private fun setActionBar() {
        supportActionBar?.title = contactDetails.name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.delete, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val optionId = item.itemId
        if (optionId == R.id.delete) {
            val returnIntent = Intent()
            returnIntent.putExtra(IS_CONTACT_DELETED, true)
            returnIntent.putExtra(DELETED_CONTACT_ID, contactId)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        if (optionId == android.R.id.home) {
            this.finish()
        }
        return true
    }
}