package com.example.splitit.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.splitit.data.Bill
import com.example.splitit.data.BillStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.splitit.data.BillCategory

class BillViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext

    private val _bills = MutableStateFlow<List<Bill>>(emptyList())
    val bills: StateFlow<List<Bill>> = _bills

    init {
        _bills.value = BillStorage.loadBills(context)
    }

    fun addBill(
        title: String,
        total: Double,
        names: List<String>,
        customAmounts: List<Double> = emptyList(),
        category: BillCategory = BillCategory.OTHER
    ) {
        val newBill = Bill(
            title = title,
            totalAmount = total,
            numberOfPeople = names.size,
            peopleNames = names,
            customAmounts = customAmounts,
            category = category
        )
        val updated = listOf(newBill) + _bills.value
        _bills.value = updated
        BillStorage.saveBills(context, updated)
    }

    fun updateBill(updatedBill: Bill) {
        val updated = _bills.value.map {
            if (it.id == updatedBill.id) updatedBill else it
        }
        _bills.value = updated
        BillStorage.saveBills(context, updated)
    }

    fun deleteBill(bill: Bill) {
        val updated = _bills.value.filter { it.id != bill.id }
        _bills.value = updated
        BillStorage.saveBills(context, updated)
    }

    fun restoreBill(bill: Bill) {
        val restored = (_bills.value + bill).sortedByDescending { it.id }
        _bills.value = restored
        BillStorage.saveBills(context, restored)
    }

    fun getBillById(id: Long): Bill? {
        return _bills.value.find { it.id == id }
    }

    fun togglePaid(bill: Bill, personIndex: Int) {
        val currentStatus = MutableList(bill.numberOfPeople) { bill.isPaid(it) }
        currentStatus[personIndex] = !currentStatus[personIndex]
        val updatedBill = bill.copy(paidStatus = currentStatus)
        updateBill(updatedBill)
    }
}