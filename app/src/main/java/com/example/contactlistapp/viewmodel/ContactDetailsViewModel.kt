package com.example.contactlistapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactlistapp.model.ContactInfo
import com.example.contactlistapp.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactDetailsViewModel @Inject constructor(private val contactRepository: ContactRepository) :
    ViewModel() {

    private var _progress: MutableLiveData<Boolean> = MutableLiveData(false)
    var progress: LiveData<Boolean> = _progress

    private var _contactDetailsData: MutableLiveData<ContactInfo> = MutableLiveData()
    var contactDetailsData: LiveData<ContactInfo> = _contactDetailsData

    fun getContactDetails(id: Int) {
        viewModelScope.launch {
            _progress.value = true
            _contactDetailsData.value = contactRepository.getContactDetails(id)
            _progress.value = false
        }
    }
}