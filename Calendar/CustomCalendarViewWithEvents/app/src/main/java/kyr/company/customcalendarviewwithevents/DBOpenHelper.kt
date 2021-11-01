package kyr.company.customcalendarviewwithevents

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi

class DBOpenHelper(context: Context) : SQLiteOpenHelper(context, DBStructure.DB_NAME, null, DBStructure.DB_VERSION) {

    companion object{
        val CREATE_EVENTS_TABLES = "CREATE TABLE "+ DBStructure.EVENT_TABLE_NAME +"("+DBStructure.ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                DBStructure.EVENT+" TEXT, "+DBStructure.TIME+" TEXT, "+DBStructure.DATE+" TEXT, "+DBStructure.MONTH+" TEXT, "+
                DBStructure.YEAR+" TEXT," +DBStructure.Notify+" TEXT"+
                ")"

        val DROP_EVENTS_TABLE = "DROP TABLE IF EXISTS "+DBStructure.EVENT_TABLE_NAME
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(CREATE_EVENTS_TABLES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(DROP_EVENTS_TABLE)
        onCreate(db)
    }

    fun SaveEvent(event:String,time:String,date:String,month:String,year:String,notify: String,database: SQLiteDatabase){
        val contentValues:ContentValues = ContentValues()
        contentValues.put(DBStructure.EVENT,event)
        contentValues.put(DBStructure.TIME,time)
        contentValues.put(DBStructure.DATE,date)
        contentValues.put(DBStructure.MONTH,month)
        contentValues.put(DBStructure.YEAR,year)
        contentValues.put(DBStructure.Notify,notify)
        database.insert(DBStructure.EVENT_TABLE_NAME,null,contentValues)
    }

    fun ReadEvents(date: String, database: SQLiteDatabase):Cursor{
        val projections: Array<String> = arrayOf(DBStructure.EVENT,DBStructure.TIME,DBStructure.DATE,DBStructure.MONTH,DBStructure.YEAR)
        val selection = DBStructure.DATE + "=?"
        val selectionArgs = arrayOf(date)
        return database.query(DBStructure.EVENT_TABLE_NAME,projections,selection,selectionArgs,null,null,null)
    }

    fun ReadIDEvents(date: String,event: String,time: String,database: SQLiteDatabase):Cursor{
        val projections: Array<String> = arrayOf(DBStructure.ID,DBStructure.Notify)
        val selection = DBStructure.DATE + "=? and "+DBStructure.EVENT+"=? and "+DBStructure.TIME+"=?"
        val selectionArgs = arrayOf(date,event,time)
        return database.query(DBStructure.EVENT_TABLE_NAME,projections,selection,selectionArgs,null,null,null)
    }


    fun ReadEventsPerMonth(month: String , year:String , database: SQLiteDatabase):Cursor{
        val projections: Array<String> = arrayOf(DBStructure.EVENT,DBStructure.TIME,DBStructure.DATE,DBStructure.MONTH,DBStructure.YEAR)
        val selection = DBStructure.MONTH + "=? and "+DBStructure.YEAR+"=?"
        val selectionArgs = arrayOf(month,year)
        return database.query(DBStructure.EVENT_TABLE_NAME,projections,selection,selectionArgs,null,null,null)
    }

    fun deleteEvent(event: String,date: String,time: String,datebase:SQLiteDatabase){
        val selection:String = DBStructure.EVENT + "=? and "+DBStructure.DATE+"=? and "+DBStructure.TIME+"=?"
        val selectionArg = arrayOf(event,date,time)
        datebase.delete(DBStructure.EVENT_TABLE_NAME,selection,selectionArg)
    }

    fun updateEvent(date: String,event: String,time: String,notify:String,database: SQLiteDatabase){
        val contentValues: ContentValues = ContentValues()
        contentValues.put(DBStructure.Notify,notify)
        val selection = DBStructure.DATE + "=? and "+DBStructure.EVENT+"=? and "+DBStructure.TIME+"=?"
//        val selection = DBStructure.DATE + "=? and "+DBStructure.TIME+"=?"
        val selectionArgs = arrayOf(date,event,time)
//        val selectionArgs = arrayOf(date,time)
        database.update(DBStructure.EVENT_TABLE_NAME,contentValues,selection,selectionArgs)
    }



}