package com.example.lab3

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.databinding.FragmentContactBinding
import com.example.lab3.databinding.FragmentInvitationBinding
import com.example.lab3.databinding.ItemLayoutContactBinding
import com.example.lab3.viewmodel.ContactViewModel

enum class contactStatus(val value:Int){
    NORMAL(0), // 正常
    INVITE(1), // 邀请
    INVITED(-1), // 被邀请
    ACCEPTED(2),    // 被同意
    REJECTED(3),    // 被拒绝
    ACCEPT(-2), // 同意邀请
    REJECT(-3)  // 拒绝邀请
}
class InvitationAdapter(val onClickStatus:(Contact, String)->Unit, val onClickRej:(Contact)->Unit): RecyclerView.Adapter<InvitationAdapter.MyViewHolder>(){
    inner class MyViewHolder(private val binding: ItemLayoutContactBinding): RecyclerView.ViewHolder(binding.root){
        init {
//            itemView.setOnClickListener { // 点击具体某个reservation时调用
//                onClick(contacts[bindingAdapterPosition])
////                println(bindingAdapterPosition)
//            }
            binding.itemTextStatus.setOnClickListener {
                onClickStatus(contacts[bindingAdapterPosition],binding.itemTextStatus.text as String)
//                if (binding.itemTextStatus.text == "0"){
//                    binding.itemTextStatus.text = "1"
//                }
            }
            binding.itemTextRej.setOnClickListener {
                if (binding.itemTextRej.text != ""){
//                    println("点击Reject")
                    onClickRej(contacts[bindingAdapterPosition])
                }
            }
        }
        fun bind(contact: Contact){
//            println("bind")
            binding.itemTextName.text = contact.name
            binding.itemTextStatus.text = ""
            binding.itemTextRej.text = ""

//            binding.itemTextStatus.text = contact.status.toString()
            when(contact.status.toString().toInt()){
                contactStatus.NORMAL.value->{     // 正常
                    binding.itemTextStatus.text = "Invite"
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
                    binding.itemTextStatus.text = "Invite"
                }
                contactStatus.ACCEPT.value->{    // 同意邀请
                    binding.itemTextStatus.text = "V"
                }
                contactStatus.REJECT.value->{    // 拒绝邀请
                    binding.itemTextStatus.text = "Invite"
                }
            }
        }

    }
    val contacts = mutableListOf<Contact>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val b = ItemLayoutContactBinding.inflate(parent.context.layoutInflater, parent, false)
        return MyViewHolder(b)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(contacts[position])
    }
}

class invitationFragment:BaseFragment(R.layout.fragment_invitation),HasBackButton {
    private val vmMain : MainViewModel by activityViewModels()
    private val sharedvm : ContactViewModel by activityViewModels()

    override val titleRes: Int = R.string.contact
    companion object {
        fun newInstance() = invitationFragment()
    }
    private lateinit var binding: FragmentInvitationBinding
    private val contacts = mutableListOf<Contact>()
    val adapter = InvitationAdapter(
        onClickStatus = {contact, status->
            println("status")
            println(contact.uid)
        },
        onClickRej = {
            println("rej")

        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedvm.getAllContact(vmMain.UID)
//        println(vmMain.UID)
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
        binding = FragmentInvitationBinding.bind(view)
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context,RecyclerView.VERTICAL,false)
    }

    private fun updateAdapter() {
        adapter.apply {
            contacts.clear()
            contacts.addAll(this@invitationFragment.contacts)
            notifyDataSetChanged() // 通知adapter发生变化，调用bind方法
        }
    }
}