package com.example.myschool.logic.model


class LoggedUser(
    var userAccount: String,
    var userPassword: String,
    var userName: String,
    var userStatic: Boolean,
    var isLogged: Boolean,
    var userHeadPortraitPath: String
)