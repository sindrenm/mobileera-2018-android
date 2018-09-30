package rocks.mobileera.mobileera.model

class Timeslot {
    var startTime: String = ""
    var endTime: String = ""
    var sessions: List<SessionItem> = ArrayList()
    var sessionsList: List<Session>? = null

    constructor(startTime: String, endTime: String, sessionsList: ArrayList<Session>?) {
        this.startTime = startTime
        this.endTime = endTime
        this.sessionsList = sessionsList
    }
}