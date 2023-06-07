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

data class Contact(val uid :String, val name:String, val status:Int)
class ContactAdapter(val onClickStatus:(Contact, String)->Unit, val onClickRej:(Contact)->Unit): RecyclerView.Adapter<ContactAdapter.ContactViewHolder>(){
    inner class ContactViewHolder(private val binding: ItemLayoutContactBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.itemTextStatus.setOnClickListener {
                onClickStatus(contacts[bindingAdapterPosition],binding.itemTextStatus.text as String)
                if (binding.itemTextStatus.text == "ACC"){
                    binding.itemTextStatus.text = "accept"
                    binding.itemTextRej.text = ""
                }
            }
            binding.itemTextRej.setOnClickListener {
                if (binding.itemTextRej.text == "REJ"){
                    onClickRej(contacts[bindingAdapterPosition])
                    binding.itemTextStatus.text = "Reject"
                    binding.itemTextRej.text = ""
                }
            }
        }
        fun bind(contact: Contact){
            binding.itemTextName.text = contact.name
            binding.itemTextStatus.text = "" // 不显示status
            binding.itemTextRej.text = ""
            when(contact.status.toString().toInt()){
                contactStatus.NORMAL.value->{     // 正常
                    binding.itemTextStatus.text = ""
                }
                contactStatus.INVITE.value->{     // 邀请
                    binding.itemTextStatus.text = "Waiting"
                }
                contactStatus.INVITED.value->{    // 被邀请
                    binding.itemTextStatus.text = "ACC"
                    binding.itemTextRej.text = "REJ"
                }
                contactStatus.ACCEPTED.value->{  // 被同意
                    binding.itemTextStatus.text = "V"
                }
                contactStatus.REJECTED.value->{  // 被拒绝
                    binding.itemTextStatus.text = "Rejected"
                }
                contactStatus.ACCEPT.value->{    // 同意邀请
                    binding.itemTextStatus.text = "V"
                }
                contactStatus.REJECT.value->{    // 拒绝邀请
                    binding.itemTextStatus.text = "Reject"
                }
            }
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

    val adapter = ContactAdapter(
        onClickStatus = {contact, status->
            when(status){
                "ACC"->{
                    sharedvm.acc(vmMain.UID, contact.uid)

                    // 给接受的用户添加reservation
                }
            }
        },
        onClickRej = {
            sharedvm.setRej(vmMain.UID,it.uid)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedvm.getAllContact(vmMain.UID)
        sharedvm.contacts.observe(this){
            contacts.clear()
            for(res in it){
                contacts.add(res)
            }
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
            notifyDataSetChanged() // 通知adapter发生变化，调用bind方法
        }
    }
}