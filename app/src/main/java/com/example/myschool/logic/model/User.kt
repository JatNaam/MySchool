package com.example.myschool.logic.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/*声明Room实体类*/
@Entity
data class User(
    var userAccount: String,
    var userPassword: String,
    var userName: String,
    var userStatic: Boolean,
    var isLogged: Boolean,
    var userHeadPortraitPath: String
):Serializable //序列化之后可以利用Intent传递对象数据
{
    /*设置主键*/
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
