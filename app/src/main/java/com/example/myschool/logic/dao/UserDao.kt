package com.example.myschool.logic.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myschool.logic.model.User

/*使用该注解，Room数据库才能把该接口识别成一个Dao*/
@Dao
interface UserDao {
    /*
    除@Query注解要编写SQL语句外，其他操作均只要添加注解即可
    */
/*
除@Query注解要编写SQL语句外，其他操作均只要添加注解即可
*/
    @Insert
    fun insertUser(user: User): Long

    @Update
    fun updateUser(newUser: User)

    @Query("select * from User")
    fun loadAllUsers(): List<User>

    @Query("select * from User where isLogged = :flag")
    fun loadLoggedUsers(flag: Boolean): List<User>

    @Query("select * from User where userAccount = :userAccount")
    fun loadUser(userAccount: String): User?

    @Query("delete from User where userAccount = :userAccount")
    fun deleteUserByAccount(userAccount: String): Int

}