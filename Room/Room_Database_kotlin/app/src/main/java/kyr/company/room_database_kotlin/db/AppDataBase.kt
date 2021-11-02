package kyr.company.room_database_kotlin.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kyr.company.room_database_kotlin.dao.WordDao
import kyr.company.room_database_kotlin.model.Word

@Database(entities = [Word::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    private class AppDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var wordDao = database.wordDao()

                    wordDao.deleteAll()

                    var word = Word("HI")
                    wordDao.insert(word)
                    word = Word("ggggg")
                    wordDao.insert(word)

                    word = Word("ABCD")
                    wordDao.insert(word)
                }
            }
        }
    }


    companion object {

        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDataBase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "word_database"
                )
                    .addCallback(AppDatabaseCallback(scope)) //초기에 데이터 넣을시 사용
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }

        }

/*        fun getDatabase(context: Context): AppDataBase {
            val tempInstacne = INSTANCE
            if (tempInstacne != null) {
                return tempInstacne
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }*/




    }

}