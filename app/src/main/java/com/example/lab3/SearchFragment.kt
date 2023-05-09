package com.example.lab3

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.database.entity.CourtTime
import com.example.lab3.databinding.CalendarDayLayoutBinding
import com.example.lab3.databinding.FragmentCalendarViewBinding

import com.example.lab3.databinding.ItemLayoutBinding

import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.WeekDayPosition
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.core.yearMonth
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekCalendarView
import com.kizitonwose.calendar.view.WeekDayBinder
import java.sql.Time
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.util.UUID

data class FreeCourt(var name:String,
                     var address:String,
                     var sport:String,
                     var startTime: Time,
                     var endTime: Time,
                     var courtTimeId: Int,
                     var courtId: Int)
class CourtAdapter(val onClick: (FreeCourt)-> Unit): RecyclerView.Adapter<CourtAdapter.CourtViewHolder>(){
    inner class CourtViewHolder(private val binding:ItemLayoutBinding):RecyclerView.ViewHolder(binding.root){
        init {

        }
        fun bind(event: FreeCourt){
            binding.itemText.text = "${event.name}  ${event.sport}  ${event.startTime}--${event.endTime}"
        }

    }
    val events = mutableListOf<FreeCourt>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourtViewHolder {
        val b = ItemLayoutBinding.inflate(parent.context.layoutInflater, parent, false)
        return CourtViewHolder(b)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: CourtAdapter.CourtViewHolder, position: Int) {
        holder.bind(events[position])
//        println("event"+events)
    }
}

class SearchFragment() : BaseFragment(R.layout.fragment_search),HasToolbar {

    val sports = listOf("running", "basketball", "swimming","pingpong","tennis")
    val vm by viewModels<CalendarViewModel>()

     override val toolbar: Toolbar?
        get() = null
    //    override val titleRes: Int? = null

    private lateinit var binding: FragmentCalendarViewBinding
    private val monthCalendarView: CalendarView get() = binding.calendarView
    private val weekCalendarView: WeekCalendarView get() = binding.weekCalendar
    private var selectedDate:LocalDate? = null
    private var sportSelected : String ="running"
    private val events = mutableMapOf<LocalDate, List<Event>>()
    private val FreeCourts = mutableMapOf<LocalDate, List<FreeCourt>>()

    val adapterC = CourtAdapter{
        println(it.sport)
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // 将res放入event数组即可！
        vm.getResBySport(this.requireActivity().application,"running")
        vm.reservations.observe(viewLifecycleOwner){
            // 从viewmodel获取数据（viewmodel从数据库拿到数据）
            for (res in it){
//                println(">>>>> $res")
                events[res.date] = events[res.date].orEmpty().plus(Event(res.resId, UUID.randomUUID().toString(), res.name, res.sport, res.startTime, res.date))
            }

        }
        vm.getAllCourtTime(this.requireActivity().application,sportSelected)
        //get all courtTime

//        vm.F.observe(viewLifecycleOwner){
//            // 从viewmodel获取数据（viewmodel从数据库拿到数据）
//            for (res in it){
////                println(">>>>> $res")
//                FreeCourts[res.sport]= FreeCourts[res.sport].orEmpty().plus(FreeCourt(res.name,res.address,res.sport,res.startTime,res.endTime,res.courtTimeId,res.courtId)) as List<FreeCourt>
//            }
//        }

//        vm.getCourtBySport(this.requireActivity().application,"running")
//        vm.Courts.observe(viewLifecycleOwner){
//            // 从viewmodel获取数据（viewmodel从数据库拿到数据）
//            for (res in it){
//                println(">>>>> $res")
//
//            }
//        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCalendarViewBinding.bind(view)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val spinnerSports = view.findViewById<Spinner>(R.id.sportSpinner)
        lateinit var sportSelectedForAddReservation : String
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sports)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerSports.adapter = adapter
        //监听下拉菜单

        spinnerSports.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
              FreeCourts.clear()
                // 获取当前选中的字符串
                val selectedSport = parent.getItemAtPosition(position).toString()
                sportSelectedForAddReservation = selectedSport
                updateCalendar(selectedSport)
                updateAdapterForDate(selectedDate)
//                println(sportSelected)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        recyclerView.adapter = adapterC

        recyclerView.layoutManager =  LinearLayoutManager(this.context, RecyclerView.VERTICAL,false )

        val currentMonth = YearMonth.now() // 2023-04
        val startMonth = currentMonth.minusMonths(100) //2014-12
        val endMonth = currentMonth.plusMonths(100) // 2031-08
        val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.SUNDAY)
        configureBinders()
//    static header: Mon|Tue|Wed|Thu|Fri|Sat|Sun
        binding.legendLayout.root.children
            .map { it as TextView }
            .forEachIndexed { index, textView ->
                textView.text = daysOfWeek[index].displayText()
                textView.setTextColorRes(R.color.example_1_white)
            }

        binding.calendarView.setup(startMonth, endMonth, daysOfWeek.first())
        binding.calendarView.scrollToMonth(currentMonth)

        weekCalendarView.setup(startMonth.atStartOfMonth(), endMonth.atEndOfMonth(), daysOfWeek.first())
        weekCalendarView.scrollToWeek(currentMonth.atStartOfMonth())

        // 箭头 下一个月
        binding.nextMonthImage.setOnClickListener {
            binding.calendarView.findFirstVisibleMonth()?.let {
                binding.calendarView.smoothScrollToMonth(it.yearMonth.nextMonth)
            }
        }
        // 箭头 上一个月
        binding.previousMonthImage.setOnClickListener {
            binding.calendarView.findFirstVisibleMonth()?.let {
                binding.calendarView.smoothScrollToMonth(it.yearMonth.previousMonth)
            }
        }

        monthCalendarView.isInvisible = binding.weekModeCheckBox.isChecked
        weekCalendarView.isInvisible = !binding.weekModeCheckBox.isChecked
        binding.weekModeCheckBox.setOnCheckedChangeListener(weekModeToggled)

        val floatingButton = view.findViewById<FloatingActionButton>(R.id.floatingAddButton)
        floatingButton.setOnClickListener {
            var sD : String? = null
            if(selectedDate != null){
                sD = selectedDate.toString()
            } else sD = LocalDate.now().toString()
            var bundle = bundleOf("date" to sD, "sport" to sportSelectedForAddReservation)
            findNavController().navigate(R.id.action_searchFragment_to_addReservationFragment,bundle)
        }

    }



    private val weekModeToggled = object :CompoundButton.OnCheckedChangeListener{
        override fun onCheckedChanged(buttonView: CompoundButton, monthToWeek: Boolean) {
            if (monthToWeek) {
                val targetDate = monthCalendarView.findFirstVisibleDay()?.date ?: return
                weekCalendarView.scrollToWeek(targetDate)
            } else {
                val targetMonth = weekCalendarView.findLastVisibleDay()?.date?.yearMonth ?: return
                monthCalendarView.scrollToMonth(targetMonth)
            }

            val weekHeight = weekCalendarView.height
            // If OutDateStyle is EndOfGrid, you could simply multiply weekHeight by 6.
            val visibleMonthHeight = weekHeight *
                    monthCalendarView.findFirstVisibleMonth()?.weekDays.orEmpty().count()

            val oldHeight = if (monthToWeek) visibleMonthHeight else weekHeight
            val newHeight = if (monthToWeek) weekHeight else visibleMonthHeight

            // Animate calendar height changes.
            val animator = ValueAnimator.ofInt(oldHeight, newHeight)
            animator.addUpdateListener { anim ->
                monthCalendarView.updateLayoutParams {
                    height = anim.animatedValue as Int
                }
                // A bug is causing the month calendar to not redraw its children
                // with the updated height during animation, this is a workaround.
                monthCalendarView.children.forEach { child ->
                    child.requestLayout()
                }
            }

            animator.doOnStart {
                if (!monthToWeek) {
                    weekCalendarView.isInvisible = true
                    monthCalendarView.isVisible = true
                }
            }
            animator.doOnEnd {
                if (monthToWeek) {
                    weekCalendarView.isVisible = true
                    monthCalendarView.isInvisible = true
                } else {
                    // Allow the month calendar to be able to expand to 6-week months
                    // in case we animated using the height of a visible 5-week month.
                    // Not needed if OutDateStyle is EndOfGrid.
                    monthCalendarView.updateLayoutParams { height =
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                }
                updateMonthTitle()
            }
            animator.duration = 250
            animator.start()
        }
    }
    private fun updateAdapterForDate(date: LocalDate?) { // 在列表中将某个date的events显示出来
//        eventsAdapter.apply {
//            events.clear()
//            events.addAll(this@SearchFragment.events[date].orEmpty())
//            notifyDataSetChanged()
//        }
//        println(FreeCourts)
        adapterC.apply {
            events.clear()
            events.addAll(this@SearchFragment.FreeCourts[date]?.distinct().orEmpty())
            notifyDataSetChanged()

        }

    }
    private fun configureBinders() {

        // set up month calender
        class DayViewContainer(view: View): ViewContainer(view) {
            lateinit var day :CalendarDay
            val binding = CalendarDayLayoutBinding.bind(view)

            //            val textView = view.findViewById<TextView>(R.id.exFiveDayText)
            init {
                view.setOnClickListener{ // click one day
                    if(day.position == DayPosition.MonthDate){ // 当前月的才能选
                        dateClicked(date = day.date)
                    }
                }
            }
        }
        monthCalendarView.dayBinder = object : MonthDayBinder<DayViewContainer>{
            // called only when a new container is needed
            override fun create(view: View) = DayViewContainer(view)

            //called every time we need to reuse a container  显示日期
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                val context = container.binding.root.context
                val textView = container.binding.dayText
                val layout = container.binding.dayLayout
                val dotView = container.binding.DotView
                bindDate(data.date, textView, dotView, layout, data.position == DayPosition.MonthDate)
//                textView.text = data.date.dayOfMonth.toString()
                if (data.position == DayPosition.MonthDate) {
                    textView.setTextColor(Color.BLACK)
//                    textView.setTextSize(16f)
                } else {
                    textView.setTextColor(Color.GRAY)
//                    textView.setTextSize(16f)
                }
            }
        }
        // 显示当前月 update title
        monthCalendarView.monthScrollListener = {updateMonthTitle();clearBackground()}
        //        binding.calendarView.monthScrollListener = { month->
//            binding.monthYearText.text = month.yearMonth.displayText()
//        }
        class WeekDayViewContainer(view:View):ViewContainer(view) {
            lateinit var day:WeekDay
            val binding = CalendarDayLayoutBinding.bind(view)
            init {
                view.setOnClickListener{ // click one day
                    if (day.position == WeekDayPosition.RangeDate){
                        dateClicked(date = day.date)
                    }

                }
            }
        }

        weekCalendarView.dayBinder = object : WeekDayBinder<WeekDayViewContainer>{
            // called only when a new container is needed
            override fun create(view: View) = WeekDayViewContainer(view)

            //called every time we need to reuse a container  显示日期
            override fun bind(container: WeekDayViewContainer, data: WeekDay) {
                container.day = data
                val context = container.binding.root.context
                val textView = container.binding.dayText
                val layout = container.binding.dayLayout
                val dotView = container.binding.DotView
                bindDate(data.date, textView, dotView, layout, data.position == WeekDayPosition.RangeDate)
//                textView.text = data.date.dayOfMonth.toString()
                textView.setTextColor(Color.BLACK)
//                textView.setTextSize(16f)
            }
        }
        weekCalendarView.weekScrollListener = { updateMonthTitle();clearBackground() }


    }
    private fun bindDate(date:LocalDate, textView: TextView, dotView:View, layout:ConstraintLayout, isSelectable:Boolean){
        textView.text = date.dayOfMonth.toString()
        if (isSelectable){

            if (selectedDate == date){ // 选择的日期
                textView.setTextColorRes(R.color.blue)
                textView.setBackgroundResource(R.drawable.selected_bg)
                dotView.makeInVisible()

            }
            else {
                textView.setTextColorRes(R.color.black)

                textView.background = null
                dotView.isVisible = !events[date].orEmpty().isNotEmpty()

            }
            if (!events[date].orEmpty().isNotEmpty()) {//对应sport当天没有任何预约
                if (FreeCourts[date] == null){

                    vm.F.observe(viewLifecycleOwner) {
                        // 从viewmodel获取数据（viewmodel从数据库拿到数据）
                        for (res in it) {
                            //it代表从数据库取出的某一个运动的所有时间段
                            //res为其中的一个时间段
//                            if(FreeCourts[date]?.get(0)?.sport!=sportSelected){  FreeCourts.clear()}
                            

                            FreeCourts[date] = FreeCourts[date].orEmpty().plus(
                                FreeCourt(
                                    res.name,
                                    res.address,
                                    res.sport,
                                    res.startTime,
                                    res.endTime,
                                    res.courtTimeId,
                                    res.courtId
                                )
                            ) as List<FreeCourt>
                        }
                    }
//                    println(FreeCourts[date])
                }
            }
            else {//无小蓝点，表示当天有预约
                val T = events[date]?.map { it.startTime }//T包含了在date这天所有对应sport预约的startTime
//                println(T!=null)
//                println(events[date])
                、
                    vm.F.observe(viewLifecycleOwner) {
                        // 从viewmodel获取数据（viewmodel从数据库拿到数据）
                        for (res in it) {
                            if (T != null) {
                                if (T.contains(res.startTime)) {//对应时间段有预约

                                } else {//对应时间段没有预约
                                   
//                      println("res="+res.sport)
                                    FreeCourts[date] = FreeCourts[date].orEmpty().plus(
                                        FreeCourt(
                                            res.name,
                                            res.address,
                                            res.sport,
                                            res.startTime,
                                            res.endTime,
                                            res.courtTimeId,
                                            res.courtId
                                        )
                                    )
                                }
                            }

//                        if(events[date]!=null){
//                            print(date)
////                            println(events[date]!=null)
//                        events[date]?.forEach(){e->
//                            FreeCourts[date]?.filter {
//                                it.startTime==e.startTime
//                            }
//
//                        }
//                    }
                        }
//                    println("fc"+FreeCourts)
//                    println(events[date])
                    }
            
            }
        }else{
            textView.background = null
            dotView.makeInVisible()

        }

    }
    private fun dateClicked(date: LocalDate) {
        if(selectedDate != date){
            val oldDate = selectedDate
            selectedDate = date
//                        println(day.date) // day.date就是点击的日期
//        Refresh both calendar views..
            monthCalendarView.notifyDateChanged(date)
            weekCalendarView.notifyDateChanged(date)
            oldDate?.let {
                monthCalendarView.notifyDateChanged(it)
                weekCalendarView.notifyDateChanged(it)

            }
            updateAdapterForDate(date) // 将当前date的events的内容显示在下面列表
        }

    }

   /* private fun addReservation(text: String) {
        if (text.isBlank()) {
            Toast.makeText(requireContext(), "Text is empty", Toast.LENGTH_LONG)
                .show()
        } else {
            selectedDate?.let {
                events[it] =
                    events[it].orEmpty().plus(Event(UUID.randomUUID().toString(), text, it))
//                updateAdapterForDate(it)
            }
        }
    }*/

    private fun updateMonthTitle() { // 更新月份

        val isMonthMode = !binding.weekModeCheckBox.isChecked
        if (isMonthMode) {
            val month = monthCalendarView.findFirstVisibleMonth()?.yearMonth ?: return
            binding.yearText.text = month.year.toString()
            binding.monthText.text = month.month.displayText(short = false)
        } else {
            val week = weekCalendarView.findFirstVisibleWeek() ?: return
            // In week mode, we show the header a bit differently because
            // an index can contain dates from different months/years.
            val firstDate = week.days.first().date
            val lastDate = week.days.last().date
            if (firstDate.yearMonth == lastDate.yearMonth) {
                binding.yearText.text = firstDate.year.toString()
                binding.monthText.text = firstDate.month.displayText(short = false)
            } else {
                binding.monthText.text =
                    firstDate.month.displayText(short = false) + " - " +
                            lastDate.month.displayText(short = false)
                if (firstDate.year == lastDate.year) {
                    binding.yearText.text = firstDate.year.toString()
                } else {
                    binding.yearText.text = "${firstDate.year} - ${lastDate.year}"
                }
            }
        }
    }
    private fun clearBackground(){
        selectedDate?.let {//切换mode的时候也会调用，但这时候不应该清除
            // clear selection if we scroll to a new month/week
            selectedDate = null
            monthCalendarView.notifyDateChanged(it)
            weekCalendarView.notifyDateChanged(it)
            updateAdapterForDate(null)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private fun updateCalendar(selectedSport: String) {
        sportSelected=selectedSport
        vm.getAllCourtTime(this.requireActivity().application,sportSelected)
        vm.getResBySport(this.requireActivity().application,selectedSport)
        vm.reservations.observe(viewLifecycleOwner){
            val oldDate=events.keys
//            events.forEach(){
//                monthCalendarView.notifyDateChanged(it.key)
//                weekCalendarView.notifyDateChanged(it.key)
//            }
            events.clear()
            oldDate.forEach(){
                monthCalendarView.notifyDateChanged(it)
                weekCalendarView.notifyDateChanged(it)
            }


            for (res in it){
  //              println(res)
                events[res.date] = events[res.date].orEmpty().plus(Event(res.resId, UUID.randomUUID().toString(), res.name, res.sport, res.startTime, res.date))
                monthCalendarView.notifyDateChanged(res.date)
                weekCalendarView.notifyDateChanged(res.date)
            }



        }

    }

    companion object {
        fun newInstance() = SearchFragment()
    }


}
