package com.example.lab3

import android.app.Application
import android.content.DialogInterface
import android.media.Image
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.database.entity.CourtTime
import com.example.lab3.database.entity.Reservation
import com.example.lab3.database.entity.ReservationFirebase
import com.example.lab3.databinding.FragmentAddReservationBinding
import com.example.lab3.databinding.FragmentProfileDetailBinding
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class AddReservationFragment : BaseFragment(R.layout.fragment_add_reservation),HasBackButton {
    private lateinit var binding: FragmentAddReservationBinding
//    override val toolbar: Toolbar?
//        get() =binding.activityToolbar
    override val titleRes: Int = R.string.addRes
    companion object {
        fun newInstance() = AddReservationFragment()
    }

    private var layoutManager : RecyclerView.LayoutManager ?= null
    private var  adapter : MyAdapter1 ?= null
    //private lateinit var viewModel: AddReservationViewModel
    private val viewModel:  AddReservationViewModel by activityViewModels()
    private val mainVm: MainViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddReservationBinding.bind(view)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView2)
        val doneButton = view.findViewById<Button>(R.id.button2)
        val description = view.findViewById<TextInputLayout>(R.id.textInputLayout)
        val dateDisplayed = view.findViewById<TextView>(R.id.textView6)
        val sportDisplayed = view.findViewById<TextView>(R.id.textView11)
        val arrowImageView = view.findViewById<ImageView>(R.id.doubleArrowDownImageView)
        val refreshSymbol = view.findViewById<ImageView>(R.id.refreshSymbol)
        val application : Application =  this.requireActivity().application
        var arrowVisible : Boolean = true
        var failed = false;
        var inFragmentAlready = false
        selectedSlot = ""

        // ANIMATIONS //
        val rotate = RotateAnimation(
            0f,
            180f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotate.duration = 1000
        rotate.interpolator = LinearInterpolator()
        refreshSymbol.visibility = View.GONE
        val disappearDelayRefresh = 1000
        //refreshSymbol.startAnimation(rotate)

        val animationSet = AnimationSet(true)

// Scale animation
        val shrinkAnimation = ScaleAnimation(1f, 0.5f, 1f, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        shrinkAnimation.duration = 1000
        animationSet.addAnimation(shrinkAnimation)
// Translation animation
        val translateAnimation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0.25f)
        translateAnimation.duration = 800
        animationSet.addAnimation(translateAnimation)
        val disappearDelay = 2500 // Delay in milliseconds before arrow disappears
        animationSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                // Animation start
            }

            override fun onAnimationEnd(animation: Animation) {
                // Animation end
                if(arrowVisible)
                    arrowImageView.startAnimation(animationSet)
            }

            override fun onAnimationRepeat(animation: Animation) {
                // Animation repeat
            }
        })
            arrowImageView.startAnimation(animationSet)

        Handler().postDelayed({
            inFragmentAlready = true
            arrowImageView.clearAnimation()
            arrowImageView.visibility = View.GONE
            arrowVisible = false
        }, disappearDelay.toLong())


        // END ANIMATIONS //

        var dateString : String? = arguments?.getString("date")
        dateDisplayed.setText(dateString)
        var sport : String? = arguments?.getString("sport")
        sportDisplayed.setText(sport)
        var dateLD : LocalDate = LocalDate.parse(dateString)

       viewModel.getTimeSlots(this.requireActivity().application,sport!!,dateLD)

        recyclerView.apply {
            recyclerView.layoutManager = LinearLayoutManager(context)
        }

        val ctLiveData = viewModel.timeSlots
        /*if(arguments?.getString("numberOfFreeCt")?.toInt() == 0){
            println("EXECUTING ANIMATION DELETION")
            arrowImageView.visibility = View.GONE
            arrowVisible = false
        }*/
        ctLiveData.observe(viewLifecycleOwner){
            recyclerView.apply {
                recyclerView.adapter = MyAdapter1(ctLiveData.value?.keys?.toList()?.sorted()!!)
            }
            if(!inFragmentAlready) {
                if (arguments?.getString("numberOfFreeCt")?.toInt() == 0) {
                    println("STOPPING ANIMATION ")
                    arrowImageView.visibility = View.GONE
                    arrowVisible = false
                }
                if (ctLiveData.value?.size == 0) {
                    println("STOPPING ANIMATION ")
                    arrowImageView.visibility = View.GONE
                    arrowVisible = false
                } else {
                    println("STARTING ANIMATION ")
                    arrowImageView.visibility = View.VISIBLE
                    arrowVisible = true
                    arrowImageView.startAnimation(animationSet)
                    Handler().postDelayed({
                        arrowImageView.clearAnimation()
                        arrowImageView.visibility = View.GONE
                        arrowVisible = false
                    }, disappearDelay.toLong())
                }
            }else {
                refreshSymbol.visibility = View.VISIBLE
                refreshSymbol.startAnimation(rotate)
                Handler().postDelayed({
                    refreshSymbol.clearAnimation()
                    refreshSymbol.visibility = View.GONE
                }, disappearDelayRefresh.toLong())
            }
        }

        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton("Confirm",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User clicked OK button
                        val splittedDate =
                            dateString?.split("-")
                        //println("DATE FROM CALENDAR : ${dateString}")
                       // println("SPLITTED DATE = ${splittedDate!![0]}  ")
                        val startTime = Timestamp(splittedDate!![0].toInt() -1900,
                            splittedDate!![1].toInt()-1,
                            splittedDate!![2].toInt(),
                            selectedSlot.toInt(),0,0,0)
                        val endTime = Timestamp(splittedDate!![0].toInt()- 1900,
                            splittedDate!![1].toInt()-1,
                            splittedDate!![2].toInt(),
                            selectedSlot.toInt()+1,0,0,0)
                        //println("START TIME = ${startTime}")
                        //println("END TIME = ${endTime}")
                        println("TIME SLOT VALUE = ${selectedSlot}")

                        val courtName = viewModel.getCourtNameBySport(application, sport)
                        val resDescription = description.editText?.text.toString()
                        val newReservation : ReservationFirebase =
                            ReservationFirebase(startTime,endTime,resDescription,courtName,sport,0,
                                "",-1)
                        try {
                            viewModel.addNewReservation(application,
                                newReservation,
                                mainVm.UID,
                                dateString,
                                selectedSlot)
                        }
                        catch (e : Exception){
                            failed = true;
                            println("Exception : ${e}")
                            Toast.makeText(this.context,"Operation failed , retry later!",Toast.LENGTH_SHORT).show()
                        }
                        if(failed == false) {
                            Toast.makeText(
                                this.context,
                                "New Reservation successfully added!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        mainVm.setShowNav(true)
                        findNavController().navigate(R.id.action_addReservationFragment_to_searchFragment)
                    })
                setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
                setTitle("Add new reservation?")
            }
            // Create the AlertDialog
            builder.create()
        }


            doneButton.setOnClickListener {
                if(selectedSlot == ""){
                    //println("SELECT A SLOT FIRST !")
                    Toast.makeText(this.context,"SELECT A SLOT FIRST !",Toast.LENGTH_SHORT).show()
                } else {
                    alertDialog?.setMessage("${sport} court on day '${dateString}' in slot $selectedSlot:00 - ${selectedSlot.toInt()+1}:00")
                    alertDialog?.show()
                    /*val splittedSelectedSlot =
                        selectedSlot.split(":") // GET THE (ex) "10" in "10:00:00" - the only thing needed for later are the two first digits
                    val timeParameters: List<Int> =
                        listOf(splittedSelectedSlot[0].toString().toInt(), 0, 0)*/
                }
                /*
                println("NEW RESERVATION : $newReservation")
                println("DESCRIPTION : ${description.editText?.text.toString()}")
                println("COURT ID : ${courtId} - COURTTIMEID : ${courtTimeId}")
                println("SELECTED SLOT : " + selectedSlot)
                println("SELECTED DATE : " + dateString)
                println("DATELD : " + dateLD.toString())
                println("SELECTED SPORT : " + sport)
                println("TIME START : " + startTime)
    */
            }

    }
}

var selectedSlot : String = ""

class MyAdapter1(val l: List<Int>) : RecyclerView.Adapter<MyAdapter1.MyViewHolder1>(){
    var singleItemSelectionPosition = -1

    inner class MyViewHolder1(v : View): RecyclerView.ViewHolder(v) {
        val tv = v.findViewById<TextView>(R.id.item_time_slot_text)

        init {
            if(getItemViewType(adapterPosition) == VIEW_TYPE_NORMAL) {
                tv.setOnClickListener {
                    setSingleSelection(adapterPosition)
                    selectedSlot = l[adapterPosition].toString()
                    //println(l[adapterPosition].startTime.toString())
                }
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder1{
        val v=LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_time_slots,parent,false)
        return MyViewHolder1(v)
    }

    override fun getItemCount(): Int {
        return if(l.isEmpty()) 1 else l.size
    }

    override fun onBindViewHolder(holder: MyViewHolder1, position: Int){
        if (getItemViewType(position) == VIEW_TYPE_EMPTY) {
            holder.tv.text = "No available slots"
            holder.tv.setBackgroundColor(0) // or set a specific background color
        } else {
            holder.tv.text =
                "${l[position].toString() + ":00"}" + " - " + "${(l[position] + 1).toString() + ":00"}"
            if (singleItemSelectionPosition == position) {
                holder.tv.setBackgroundColor(holder.tv.resources.getColor(R.color.blue))
            } else {
                holder.tv.setBackgroundColor(0)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (l.isEmpty()) VIEW_TYPE_EMPTY else VIEW_TYPE_NORMAL
    }

    companion object {
        private const val VIEW_TYPE_EMPTY = 0
        private const val VIEW_TYPE_NORMAL = 1
    }

    private fun setSingleSelection(adapterPosition: Int){
        if(adapterPosition == RecyclerView.NO_POSITION) return

        notifyItemChanged(singleItemSelectionPosition)
        singleItemSelectionPosition = adapterPosition
        notifyItemChanged(singleItemSelectionPosition)
    }
}


