package com.example.contactlistapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactlistapp.model.ContactInfo
import com.example.contactlistapp.model.DataState
import com.example.contactlistapp.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(private val contactRepository: ContactRepository) :
    ViewModel() {

    private var _contactListData: MutableLiveData<List<ContactInfo>> = MutableLiveData()
    var contactListData: LiveData<List<ContactInfo>> = _contactListData

    private var _progress: MutableLiveData<Boolean> = MutableLiveData(false)
    var progress: LiveData<Boolean> = _progress

    fun getAllContacts() {
        viewModelScope.launch {
            _progress.value = true
            _contactListData.value = contactRepository.getAllContacts()
            _progress.value = false
        }
    }
}