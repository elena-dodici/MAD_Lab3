package com.example.lab3

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class startTimeAdapter(var context : Context, var arrayList: ArrayList<String> ): BaseAdapter() {
    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(p0: Int): Any {
        return arrayList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return arrayList[p0].toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view: View  = View.inflate(context,R.layout.freeslot_item,null)
        var start : TextView = view.findViewById( R.id.st)
        start.text =   arrayList[p0]
        return view
    }


}