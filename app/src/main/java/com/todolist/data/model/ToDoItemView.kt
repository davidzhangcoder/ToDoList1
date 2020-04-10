package com.todolist.data.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ToDoItemView (

        @Embedded
        val toDoItem: ToDoItem,

        //One-To-Many
        //@Relation注解 不能用于@Entity注解的实体类中
        @Relation(
                // entity 标示关联查询的表（非必须），默认匹配返回类型的表
                entity = ToDoImage::class,
                // parentColumn 表示 Course 表中的字段（可以是Course表中的任意字段）
                // entityColumn 表示 Teacher表的 用于 查询的字段(可以是Teacher表中的任意字段)
                // 最后的子查询语句是 （example：SELECT `tid`,`name`,`cid` FROM `tab_teacher` where :entityColumn in [:parentColumn]）
                parentColumn = "id",
                entityColumn = "todoitem_id"
        )
        var toDoImageList: List<ToDoImage>

) :
        Parcelable {

}