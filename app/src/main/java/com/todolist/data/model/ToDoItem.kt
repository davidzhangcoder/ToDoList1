package com.todolist.data.model

import android.os.Parcelable
import androidx.room.*
import com.todolist.model.IToDoItem
import com.todolist.model.IToDoItemType
import com.todolist.model.ITypeFactory
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(
        tableName = "todo_item",
        foreignKeys = [ForeignKey(
                entity = ToDoCategory::class,
                childColumns = ["category_id"],
                parentColumns = ["id"],
                onDelete = ForeignKey.SET_NULL
        )],
        indices = [
            Index("id"),
            Index("category_id")
        ]
)
//The reason not using data class here is it need to declare open - useless comment here ( implementation changed )
@Parcelize
data class ToDoItem(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Long = 0,

        @ColumnInfo(name = "name")
        var name: String = "",

        @ColumnInfo(name = "due_timestamp")
        var dueTimestamp: Long = 0,

        @ColumnInfo(name = "remind_timestamp")
        var remindTimestamp: Long = 0,

        @ColumnInfo(name = "remark")
        var remark: String = "",

        @ColumnInfo(name = "done_indicator")
        var isDone: Boolean = false,

        @ColumnInfo(name = "recurrence_period")
        var recurrencePeriod: Int = 0,

        @ColumnInfo(name = "category_id")
        var toDoCategoryID: Long = 0
 ) :
        IToDoItemType,
        IToDoItem,
        Parcelable {

    @IgnoredOnParcel
    @Ignore
    var dueDate : Calendar? = null
    get() {
        if( field == null && this.dueTimestamp != 0L ) {
            val date : Calendar= Calendar.getInstance();
            date.setTimeInMillis(dueTimestamp);
            field = date;
        }
        return field;
    };

    @Ignore
    var remindDate : Calendar? = null
    get() {
        if( field == null && this.remindTimestamp != 0L ) {
            val date : Calendar= Calendar.getInstance();
            date.setTimeInMillis(remindTimestamp);
            field = date;
        }
        return field;
    }

    @Ignore
    var toDoCategory : ToDoCategory? = null;

    @Ignore
    var toDoImageList : ArrayList<ToDoImage> = ArrayList<ToDoImage>();

    override fun type(typeFactory: ITypeFactory): Int {
        return typeFactory.getType(this);
    }

}