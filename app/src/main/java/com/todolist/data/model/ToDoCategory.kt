package com.todolist.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.todolist.R
import com.todolist.context.ContextHolder
import com.todolist.model.IToDoItemType
import com.todolist.model.ITypeFactory
import com.todolist.util.ToDoItemUtil
import java.io.Serializable

@Entity(tableName = "todo_category")
//The reason not using data class here is it need to add some logic in get/set name()
class ToDoCategory( _name: String)
    : IToDoItemType,
        Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0;

    @ColumnInfo(name = "name")
    var name: String = _name
        set(value) {
            field = ToDoItemUtil.getCategoryName(value);
        }

    init {

    }

    constructor(id: Long, name: String) : this( name ) {
        this.id = id
    }

    @Ignore
    var selected: Boolean = false;

    companion object {
        val CATEGORY_ALL_ID: Int = -2;

        fun getAllCatrgoryName(): String {
            return ContextHolder.getContext().resources.getString(R.string.category_all_display);
        }
    }

    override fun type(typeFactory: ITypeFactory): Int {
        return typeFactory.getType(this);
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ToDoCategory

        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

}