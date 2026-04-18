package com.example.splitit.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.splitit.data.Bill
import com.example.splitit.data.BillStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BillViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext

    private val _bills = MutableStateFlow<List<Bill>>(emptyList())
    val bills: StateFlow<List<Bill>> = _bills

    init {
        _bills.value = BillStorage.loadBills(context)
    }

    fun addBill(title: String, total: Double, names: List<String>) {
        val newBill = Bill(
            title = title,
            totalAmount = total,
            numberOfPeople = names.size,
            peopleNames = names
        )
        val updated = listOf(newBill) + _bills.value
        _bills.value = updated
        BillStorage.saveBills(context, updated)
    }

    fun deleteBill(bill: Bill) {
        val updated = _bills.value.filter { it.id != bill.id }
        _bills.value = updated
        BillStorage.saveBills(context, updated)
    }

    fun getBillById(id: Long): Bill? {
        return _bills.value.find { it.id == id }
    }

    fun updateBill(updatedBill: Bill) {
        val updated = _bills.value.map {
            if (it.id == updatedBill.id) updatedBill else it
        }
        _bills.value = updated
        BillStorage.saveBills(context, updated)
    }
}