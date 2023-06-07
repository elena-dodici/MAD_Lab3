package com.example.lab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.databinding.FragmentCalendarViewBinding
import com.example.lab3.databinding.FragmentContactBinding
import com.example.lab3.databinding.ItemLayoutBinding
import com.example.lab3.databinding.ItemLayoutContactBinding
import com.example.lab3.viewmodel.ContactViewModel

data class Contact(val name:String, val status:Int)
class ContactAdapter(val onClick: (Contact)-> Unit): RecyclerView.Adapter<ContactAdapter.ContactViewHolder>(){
    inner class ContactViewHolder(private val binding: ItemLayoutContactBinding): RecyclerView.ViewHolder(binding.root){
        init {
            itemView.setOnClickListener { // 点击具体某个reservation时调用
                onClick(contacts[bindingAdapterPosition])
//                println(bindingAdapterPosition)
            }
        }
        fun bind(contact: Contact){
            binding.itemTextName.text = contact.name
            binding.itemTextStatus.text = "" // 不显示status
            binding.itemTextRej.text = ""
        }

    }
    val contacts = mutableListOf<Contact>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val b = ItemLayoutContactBinding.inflate(parent.context.layoutInflater, parent, false)
        return ContactViewHolder(b)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contacts[position])
    }
}

class ContactFragment:BaseFragment(R.layout.fragment_contact),HasBackButton {
    private val vmMain : MainViewModel by activityViewModels()
    private val sharedvm : ContactViewModel by activityViewModels()

    override val titleRes: Int = R.string.contact
    companion object {
        fun newInstance() = ContactFragment()
    }
    private lateinit var binding:FragmentContactBinding
    private val contacts = mutableListOf<Contact>()
    val adapter = ContactAdapter{

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedvm.getAllContact(vmMain.UID)
//        println(vmMain.UID)
        sharedvm.contacts.observe(this){
            contacts.clear()
            for(res in it){
//                println(res)
                contacts.add(res)
            }
            println(">> $contacts")
            updateAdapter()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentContactBinding.bind(view)
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context,RecyclerView.VERTICAL,false)
    }

    private fun updateAdapter() {
        adapter.apply {
            contacts.clear()
            contacts.addAll(this@ContactFragment.contacts)
            println("2 $contacts")
            notifyDataSetChanged() // 通知adapter发生变化，调用bind方法
        }
    }
}