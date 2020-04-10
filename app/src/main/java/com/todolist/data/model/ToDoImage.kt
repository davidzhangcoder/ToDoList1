package com.todolist.data.model

import android.net.Uri
import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Entity(
        tableName = "todo_image",
        foreignKeys = [ForeignKey(
                entity = ToDoItem::class,
                childColumns = ["todoitem_id"],
                parentColumns = ["id"],
                onDelete = ForeignKey.CASCADE
        )],
        indices = [
            Index("id"),
            Index("todoitem_id")
        ]
)
@Parcelize
data class ToDoImage(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Long = 0,

        @ColumnInfo(name = "url")
        var url: String = "",

        @ColumnInfo(name = "todoitem_id")
        var toDoItemId: Long = 0
) : Parcelable {

    @Ignore
    var isAdd : Boolean = false;

    @Ignore
    lateinit var uri : Uri;

}