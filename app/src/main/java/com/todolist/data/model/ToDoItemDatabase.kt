package com.todolist.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.todolist.R
import com.todolist.app.App
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(
        entities = [
            ToDoItem::class,
            ToDoCategory::class,
            ToDoImage::class],
        version = 1,
        exportSchema = false)
abstract class ToDoItemDatabase : RoomDatabase() {

    abstract fun getToDoItemDao() : ToDoItemDao;

    companion object {

        private var INSTANCE: ToDoItemDatabase? = null

        private val lock = Any()

        private val NUMBER_OF_THREADS : Int = 4;

        private val executer : ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        fun getInstance(context: Context): ToDoItemDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            ToDoItemDatabase::class.java, "todo_list_db")
                            .addCallback( roomCallBack )
                            .build()
                }
                return INSTANCE!!
            }
        }

        private val roomCallBack : RoomDatabase.Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                executer.execute(
                        {
                            ->
                            db.beginTransaction();
                            try {
                                initCategory();
                                db.setTransactionSuccessful();
                            } finally {
                                db.endTransaction();
                            }
                        }
                );
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
            }

            override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                super.onDestructiveMigration(db)
            }
        }

        private fun initCategory()
        {
            if( INSTANCE != null ) {
                val toDoItemDao : ToDoItemDao = INSTANCE!!.getToDoItemDao();

                val toDoCategory1 : ToDoCategory = ToDoCategory( App.getContext().getString(R.string.category_working) );
                toDoItemDao.insertToDoCategory( toDoCategory1 );

                val toDoCategory2 : ToDoCategory = ToDoCategory( App.getContext().getString(R.string.category_learning) );
                toDoItemDao.insertToDoCategory( toDoCategory2 );

                val toDoCategory3 : ToDoCategory = ToDoCategory( App.getContext().getString(R.string.category_meeting) );
                toDoItemDao.insertToDoCategory( toDoCategory3 );

                val toDoCategory4 : ToDoCategory = ToDoCategory( App.getContext().getString(R.string.category_appointment) );
                toDoItemDao.insertToDoCategory( toDoCategory4 );

                val toDoCategory5 : ToDoCategory = ToDoCategory( App.getContext().getString(R.string.category_shopping) );
                toDoItemDao.insertToDoCategory( toDoCategory5 );

                val toDoCategory6 : ToDoCategory = ToDoCategory( App.getContext().getString(R.string.category_other) );
                toDoItemDao.insertToDoCategory( toDoCategory6 );

            }
        }

    }

}