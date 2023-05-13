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
import com.example.lab3.database.entity.FreeCourt
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


class CourtAdapter(): RecyclerView.Adapter<CourtAdapter.CourtViewHolder>(){
    inner class CourtViewHolder(private val binding:ItemLayoutBinding):RecyclerView.ViewHolder(binding.root){
        init {        }
        fun bind(event: FreeCourt){ // 显示到recyclerview
            binding.itemText.text = "${event.name}  ${event.sport}  ${event.startTime}--${event.endTime}"
        }

    }
    val events = mutableListOf<FreeCourt>() // 内部变量，要显示的FreeCourt存在这个数组中
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourtViewHolder {
        val b = ItemLayoutBinding.inflate(parent.context.layoutInflater, parent, false)
        return CourtViewHolder(b)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: CourtAdapter.CourtViewHolder, position: Int) {
        holder.bind(events[position]) // 将events[position]显示出来
    }
}

class SearchFragment : BaseFragment(R.layout.fragment_search),HasToolbar {

    val sports = listOf("running", "basketball", "swimming","pingpong","tennis")
    val allStartTime = mutableListOf<Time>()
    val vm : CalendarViewModel by activityViewModels()


    override val toolbar: Toolbar?
        get() = null
    companion object {
        fun newInstance() = SearchFragment()
    }
    private lateinit var binding: FragmentCalendarViewBinding
    private val monthCalendarView: CalendarView get() = binding.calendarView
    private val weekCalendarView: WeekCalendarView get() = binding.weekCalendar
    private var selectedDate:LocalDate? = null
    private var selectedSport : String = sports[0] // default sport
//    private val events = mutableMapOf<LocalDate, List<Event>>() // 已有的预定
    private val reservations = mutableMapOf<LocalDate, List<Event>>() // 已有的预定
    private val freeCourts = mutableMapOf<LocalDate, List<FreeCourt>>() // 空闲的时间段，要维护来显示到recyclerview中的 通过传入adapter中来显示的屏幕上
    private val allReserved = mutableMapOf<LocalDate,Boolean>() // 某一天是否完全被预定了
    val adapterC = CourtAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // init
        reservations.clear()
        allReserved.clear()
        freeCourts.clear()
        allStartTime.clear()
        vm.getAllCourtTime(this.requireActivity().application,selectedSport)
        vm.F.observe(viewLifecycleOwner){ // 为啥被调用2次？？？
            allStartTime.clear()
            for (slot in it){
                allStartTime.add(slot.startTime)
            }
        }
        vm.getResBySport(this.requireActivity().application,selectedSport) // 当前运动所有预定信息
        vm.reservations.observe(viewLifecycleOwner){// 为啥被调用2次？？？
            // 所有的预约信息
            for (res in it){
                reservations[res.date] = reservations[res.date].orEmpty().plus(Event(res.resId, UUID.randomUUID().toString(), res.name, res.sport, res.startTime, res.date))
                allReserved[res.date] = (allStartTime.size == reservations[res.date]!!.size) // 若所有时间段数== 当前运动这一天的预定数，说明这一天被预定满了
            }
        }
        // 不需要知道每天的free，只要知道哪天没有free即可（用于标记），剩下的点击那一天的时候再获取free slot显示出来即可
        // 一开始就可以知道哪天已经预约满了(那一天就没有free
        // 对于每一个reservation里有的日期，我可以知道这一天的free slot
        // 剩余的所有日期，每个时间段都应该可用

//        freeCourts.clear()
        //get all courtTime

        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCalendarViewBinding.bind(view)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapterC
        recyclerView.layoutManager =  LinearLayoutManager(this.context, RecyclerView.VERTICAL,false )

        // 下拉菜单
        val spinnerSports = view.findViewById<Spinner>(R.id.sportSpinner)
//        lateinit var sportSelectedForAddReservation : String
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sports)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSports.adapter = adapter
        //监听下拉菜单
        spinnerSports.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
//                reservations.clear() // 清空小蓝点
                freeCourts.clear()   // 更新列表
                // 获取当前选中的字符串
                selectedSport = parent.getItemAtPosition(position).toString()
                updateCalendar(selectedSport) // 拿到了新的预定信息  某个date如果满了就不能点了(在bind实现
                if(selectedDate != null)
                    getFreeSlot(selectedSport,selectedDate)  // 拿到当前天的free slot
                updateAdapterForDate(selectedDate)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }



        val currentMonth = YearMonth.now() // 2023-04
        val startMonth = currentMonth.minusMonths(100) //2014-12
        val endMonth = currentMonth.plusMonths(100) // 2031-08
        val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.SUNDAY)
        configureBinders()
//    static header: Sun|Mon|Tue|Wed|Thu|Fri|Sat|
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
            var bundle = bundleOf("date" to sD, "sport" to selectedSport)
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
    private fun updateAdapterForDate(date: LocalDate?) { // 填充会被展示在recyclerview的events数组
        adapterC.apply {
            events.clear()
            events.addAll(this@SearchFragment.freeCourts[date].orEmpty())
            notifyDataSetChanged()

        }

    }
    private fun configureBinders() {

        // set up month calender
        class DayViewContainer(view: View): ViewContainer(view) {
            lateinit var day :CalendarDay
            val binding = CalendarDayLayoutBinding.bind(view)
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
//                bindDate(data.date, textView, layout, data.position == DayPosition.MonthDate)
//                textView.text = data.date.dayOfMonth.toString()
                if (data.position == DayPosition.MonthDate) {
                    textView.setTextColor(Color.BLACK)
                } else {
                    textView.setTextColor(Color.GRAY)
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
//                bindDate(data.date, textView, layout, data.position == WeekDayPosition.RangeDate)
//                textView.text = data.date.dayOfMonth.toString()
                textView.setTextColor(Color.BLACK)
//                textView.setTextSize(16f)
            }
        }
        weekCalendarView.weekScrollListener = { updateMonthTitle();clearBackground() }


    }
    private fun bindDate(date:LocalDate, textView: TextView, dotView:View, layout:ConstraintLayout, isSelectable:Boolean){
        textView.text = date.dayOfMonth.toString()
        dotView.makeInVisible()
        if (isSelectable){

            if (selectedDate == date){ // 选择的日期
                textView.setTextColorRes(R.color.blue)
                textView.setBackgroundResource(R.drawable.selected_bg)
//                dotView.makeInVisible()

            }
            else {
                textView.setTextColorRes(R.color.black)

                textView.background = null
//                dotView.isVisible = reservations[date].orEmpty().isEmpty()
// 不显示小蓝点了，改成如果不能预约就禁用那一天
            }
            layout.isClickable = allReserved[date] != true
            if (allReserved[date] == true){
                layout.setBackgroundColor(Color.LTGRAY)
            }else{
                layout.setBackgroundColor(Color.WHITE)
            }
//            if (reservations[date].orEmpty().isEmpty()) {//对应sport当天没有任何预约
//                if (freeCourts[date] == null){
//
//                    vm.F.observe(viewLifecycleOwner) {
//                        // 从viewmodel获取数据（viewmodel从数据库拿到数据）
//                        for (res in it) {
//                            //it代表从数据库取出的某一个运动的所有时间段
//                            //res为其中的一个时间段
////                            if(freeCourts[date]?.get(0)?.sport!=sportSelected){  freeCourts.clear()}
//
//                            freeCourts[date] = freeCourts[date].orEmpty().plus(
//                                FreeCourt(res.name, res.address, res.sport, res.startTime, res.endTime, res.courtTimeId, res.courtId )
//                            )
//                        }
//                    }
//                }
//            }
//            else {//无小蓝点，表示当天有预约
//                val T = reservations[date]?.map { it.startTime }//T包含了在date这天所有对应sport预约的startTime
//
//                freeCourts[date] = listOf<FreeCourt>()
//
//                vm.F.observe(viewLifecycleOwner) {
//                    // 从viewmodel获取数据（viewmodel从数据库拿到数据）
//                    for (res in it) {
//                        if (T != null) {
//                            if (T.contains(res.startTime)) {//对应时间段有预约
//
//                            } else {//对应时间段没有预约
//
//                                freeCourts[date] = freeCourts[date].orEmpty().plus(
//                                    FreeCourt(res.name, res.address, res.sport, res.startTime, res.endTime, res.courtTimeId, res.courtId )
//                                )
//
//                            }
//                        }
//
//                    }
//
//                }
//
//            }
        }else{
            textView.background = null
//            dotView.makeInVisible()
            textView.setBackgroundColor(Color.WHITE)


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
            getFreeSlot(selectedSport, date)
//            println(freeCourts[date])
            updateAdapterForDate(date) // 将当前date的空闲时间段的内容显示在下面列表
        }

    }



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
        selectedDate?.let {
            // clear selection if we scroll to a new month/week
            selectedDate = null
            monthCalendarView.notifyDateChanged(it)
            weekCalendarView.notifyDateChanged(it)
            updateAdapterForDate(null)
        }
    }

    private fun updateCalendar(sport:String) { // 获取新的运动的预定信息-> 调用日历的bind
//        vm.getAllCourtTime(this.requireActivity().application,sport) // 根据sport获取相关的court和所有的时间段 保存到F中
        vm.getResBySport(this.requireActivity().application,sport)   // 根据sport获取预定信息
        vm.reservations.observe(viewLifecycleOwner){
            val oldDate=reservations.keys
            oldDate.forEach(){
                monthCalendarView.notifyDateChanged(it) // 调用monthCalendarView的bind
                weekCalendarView.notifyDateChanged(it)
            }

            reservations.clear()
            allReserved.clear()
            for (res in it){
                reservations[res.date] = reservations[res.date].orEmpty().plus(Event(res.resId, UUID.randomUUID().toString(), res.name, res.sport, res.startTime, res.date))
                monthCalendarView.notifyDateChanged(res.date)
                weekCalendarView.notifyDateChanged(res.date)
                allReserved[res.date] = (allStartTime.size == reservations[res.date]!!.size) // 若所有时间段数== 当前运动这一天的预定数，说明这一天被预定满了

            }

        }

    }

    private fun getFreeSlot(sport: String, date: LocalDate?){
//        freeCourts.clear() // 清除旧的
//        freeCourts[date!!] = listOf() // 清空这一天原本的free slot
        vm.getFreeSlotBySportAndDate(sport, date!!,this.requireActivity().application)
        vm.freeSlots.observe(this){
            this.freeCourts[date] = it
        }
    }




}
