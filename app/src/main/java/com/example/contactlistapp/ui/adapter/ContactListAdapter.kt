package com.example.contactlistapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.contactlistapp.databinding.ContactCardBinding
import com.example.contactlistapp.model.ContactInfo
import java.util.Objects

class ContactListAdapter(
    private var list: List<ContactInfo>,
    private val onContactLongCardClicked: (Int) -> Unit,
    private val onContactCardClicked: () -> Unit
) :
    RecyclerView.Adapter<ContactListAdapter.MyViewHolder>() {

    var selectedContactId: Int = 0


    inner class MyViewHolder(private val binding: ContactCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(item: ContactInfo) {
            binding.name.text = item.name
            binding.email.text = item.email
            binding.cardContainer.setOnLongClickListener {
                onContactLongCardClicked.invoke(item.id)
                return@setOnLongClickListener true
            }
            binding.cardContainer.setOnClickListener {
                selectedContactId = item.id
                onContactCardClicked.invoke()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ContactCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    fun updateList(contactList: List<ContactInfo>) {
        list = contactList
        notifyDataSetChanged()
    }

    fun removeContact(id: Int) {
        val filteredList = mutableListOf<ContactInfo>()
        list.forEach {
            if (it.id != id)
                filteredList.add(it)
        }
        updateList(filteredList as List<ContactInfo>)
    }

    fun filterList(filterlist: ArrayList<ContactInfo>) {
        // below line is to add our filtered
        // list in our course array list.
        updateList(filterlist)
        // below line is to notify our adapter
        // as change in recycler view data.
    }

    fun getCurrentList(): List<ContactInfo> {
        return list
    }
}