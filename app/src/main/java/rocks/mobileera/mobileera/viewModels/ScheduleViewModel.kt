package rocks.mobileera.mobileera.viewModels

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.google.firebase.database.*
import rocks.mobileera.mobileera.model.Day
import rocks.mobileera.mobileera.model.Speaker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import rocks.mobileera.mobileera.model.Session
import rocks.mobileera.mobileera.model.Timeslot
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


class ScheduleViewModel : ViewModel() {

    private var database: DatabaseReference? = null

    init {
        database = FirebaseDatabase.getInstance().reference
        database?.keepSynced(true)
    }

    private var days: MutableLiveData<List<Day>>? = null
    fun getDays(): MutableLiveData<List<Day>>? {
        if (days == null) {
            days = MutableLiveData()
            loadSchedule()
        }

        return days
    }

    var showOnlyFavorite: MutableLiveData<Boolean> = MutableLiveData()
    var selectedTags: MutableLiveData<List<String>> = MutableLiveData()
    var allTags: ArrayList<String> = ArrayList()

    private fun loadSchedule() {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US)

        val scheduleListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val gson = Gson()

                val speakersSnapshot = snapshot.child("speakers").value
                val sessionsSnapshot = snapshot.child("sessions").value
                val scheduleSnapshot = snapshot.child("schedule").value

                if (speakersSnapshot == null || sessionsSnapshot == null ||
                       scheduleSnapshot == null) {
                    return
                }

                val speakersJson = gson.toJson(speakersSnapshot)
                val sessionsJson = gson.toJson(sessionsSnapshot)
                val scheduleJson = gson.toJson(scheduleSnapshot)

                if (speakersJson == null || sessionsJson == null ||
                        scheduleJson == null) {
                    return
                }

                val speakersDictionary: Map<String, Speaker> = gson.fromJson(speakersJson, object : TypeToken<Map<String, Speaker>>() {}.type)
                val sessionsDictionary: Map<String, Session> = gson.fromJson(sessionsJson, object : TypeToken<Map<String, Session>>() {}.type)
                val scheduleDictionary: Map<String, Day> = gson.fromJson(scheduleJson, object : TypeToken<Map<String, Day>>() {}.type)



                for (speaker in speakersDictionary) {
                    speaker.value.id = speaker.key
                }

                for (session in sessionsDictionary) {
                    session.value.id = session.key.toInt()
                }

                for (day in scheduleDictionary) {
                    day.value.date = day.key
                }

                val speakers = speakersDictionary.values.toTypedArray()
                val sessions = sessionsDictionary.values.toTypedArray()
                var schedule: MutableList<Day> = scheduleDictionary.values.toMutableList()

                schedule = schedule.sortedBy { it.date }.toMutableList()



                var allTagsList: HashSet<String> = HashSet()
                for (session in sessions) {
                    val joinedSpeakerList: ArrayList<Speaker> = ArrayList()

                    session.speakers.forEach { id ->
                        speakers.firstOrNull { it.id == id }?.let {
                            joinedSpeakerList.add(it)
                        }

                    }

                    session.speakersList = joinedSpeakerList
                    session.tags?.forEach {tag ->  allTagsList.add(tag) }
                }


                for (day in schedule) {
                    day.timeslots?.forEach { timeslot ->
                        val joinedSessionsList: ArrayList<Session> = ArrayList()

                        timeslot.sessions.map { session -> session.items.first() }.forEach {id ->
                            sessions.firstOrNull { talk ->
                                talk.id == id
                            }?.let { joinedSession->
                                joinedSession.startDate = dateFormatter.parse(day.date + "T" + timeslot.startTime)
                                joinedSession.endDate = dateFormatter.parse(day.date + "T" + timeslot.endTime)
                                joinedSessionsList.add(joinedSession)
                            }
                        }

                        timeslot.sessionsList = joinedSessionsList
                    }
                }


                // Populating a schedule for the workshops
                val workshopDay = Day()
                val timeslots = ArrayList<Timeslot>()
                timeslots.add(Timeslot("09:00", "16:00", ArrayList(sessions.filter { it.isWorkshop() })))
                workshopDay.timeslots = timeslots
                schedule.add(0, workshopDay)


                allTags = ArrayList(allTagsList)
                days?.value = schedule
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadSchedule:onCancelled ${databaseError.toException()}")
            }
        }

        database?.addValueEventListener(scheduleListener)
    }

}
