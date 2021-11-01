package kyr.company.customcalendarviewwithevents

class Events constructor(){

    var EVENT : String = ""
    var TIME : String = ""
    var DATE : String = ""
    var MONTH : String = ""
    var YEAR : String = ""

    constructor(EVENT:String,TIME:String,DATE:String,MONTH:String,YEAR:String):this(){
        this.EVENT=EVENT
        this.TIME=TIME
        this.DATE=DATE
        this.MONTH=MONTH
        this.YEAR=YEAR
    }

}