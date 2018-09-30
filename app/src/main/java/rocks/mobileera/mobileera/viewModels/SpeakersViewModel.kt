package rocks.mobileera.mobileera.viewModels

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import rocks.mobileera.mobileera.model.Speaker


class SpeakersViewModel : ViewModel() {

    private var database: DatabaseReference? = null

    init {
        database = FirebaseDatabase.getInstance().reference
        database?.keepSynced(true)
    }

    private var speakers: MutableLiveData<List<Speaker>>? = null
    fun getSpeakers(): MutableLiveData<List<Speaker>>? {
        if (speakers == null) {
            speakers = MutableLiveData()
            loadSpeakers()
        }

        return speakers
    }

    private fun loadSpeakers() {

        val speakersListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val gson = Gson()

                val speakersSnapshot = snapshot.child("speakers").value ?: return
                val speakersJson = gson.toJson(speakersSnapshot) ?: return
                val speakersDictionary: Map<String, Speaker> = gson.fromJson(speakersJson, object : TypeToken<Map<String, Speaker>>() {}.type)

                for (speaker in speakersDictionary) {
                    speaker.value.id = speaker.key
                }

                speakers?.value = speakersDictionary.values.sortedBy { speaker -> speaker.name }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadSpeakers:onCancelled ${databaseError.toException()}")
            }
        }

        database?.addValueEventListener(speakersListener)
    }

}