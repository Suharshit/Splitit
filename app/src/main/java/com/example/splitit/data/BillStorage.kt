package com.example.splitit.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object BillStorage {
    private const val FILE_NAME = "bills.json"

    fun saveBills(context: Context, bills: List<Bill>) {
        val array = JSONArray()
        bills.forEach { bill ->
            val obj = JSONObject()
            obj.put("id", bill.id)
            obj.put("title", bill.title)
            obj.put("totalAmount", bill.totalAmount)
            obj.put("numberOfPeople", bill.numberOfPeople)
            val namesArray = JSONArray()
            bill.peopleNames.forEach { namesArray.put(it) }
            obj.put("peopleNames", namesArray)
            array.put(obj)
        }
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
            it.write(array.toString().toByteArray())
        }
    }

    fun loadBills(context: Context): List<Bill> {
        return try {
            val json = context.openFileInput(FILE_NAME).bufferedReader().readText()
            val array = JSONArray(json)
            val bills = mutableListOf<Bill>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val namesArray = obj.optJSONArray("peopleNames")
                val names = mutableListOf<String>()
                if (namesArray != null) {
                    for (j in 0 until namesArray.length()) {
                        names.add(namesArray.getString(j))
                    }
                }
                bills.add(
                    Bill(
                        id = obj.getLong("id"),
                        title = obj.getString("title"),
                        totalAmount = obj.getDouble("totalAmount"),
                        numberOfPeople = obj.getInt("numberOfPeople"),
                        peopleNames = names
                    )
                )
            }
            bills.sortedByDescending { it.id }
        } catch (e: Exception) {
            emptyList()
        }
    }
}