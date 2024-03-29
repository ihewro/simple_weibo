package com.neo.sk.todos2018.front

/**
  * Created by haoshuhan on 2018/6/4.
  */
object Routes {
  val base = "/todos2018"

  object Login{
    val baseUrl = base + "/login"
    val userLogin = baseUrl + "/userLogin"
    val userLogout = baseUrl + "/userLogout"
    var userRegister = baseUrl + "/userRegister"
  }

  object List {
    val baseUrl = base + "/list"
    val getRecordListByLoginUser = baseUrl + "/getRecordListByLoginUser"
    val getFocusRecordList = baseUrl + "/getFocusRecordList"
    val getRecentHotList = baseUrl + "/getRecentHotList"
    val goComment = baseUrl + "/goComment"
    val getCommentListByRecordId = baseUrl+"/getCommentListByRecordId"
    val getRecordById = baseUrl+"/getRecordById"
    val getRecordListByUser = baseUrl+"/getRecordListByUser"
    val addRecord = baseUrl + "/addRecord"
    val delRecord = baseUrl + "/delRecord"
    val addComment = baseUrl + "/addComment"
  }

  /**
   * @see package com.neo.sk.todos2018.service.UserService
   */
  object User{
    val baseUrl = base + "/user"
    val getConcernList = baseUrl +"/getConcernList"
    val getFansList = baseUrl +"/getFansList"
    val getLikedUserListByRecordId = baseUrl + "/getLikedUserListByRecordId"
    val addOrCancelLike = baseUrl + "/addOrCancelLike"
    val getCurrentUser = baseUrl + "/getCurrentUser"
    val addOrCancelFocus = baseUrl + "/addOrCancelFocus"
    val editProfile = baseUrl + "/editProfile"
    val getAvatarList = baseUrl + "/getAvatarList"
    val getRecommendUserList = baseUrl + "/getRecommendUserList"

  }
}
