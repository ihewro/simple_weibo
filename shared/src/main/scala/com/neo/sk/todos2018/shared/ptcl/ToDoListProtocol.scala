package com.neo.sk.todos2018.shared.ptcl

/**
  * User: sky
  * Date: 2018/6/1
  * Time: 15:45
  *
  * update by zhangtao: 2019-3-23
  *
  */
object ToDoListProtocol {

  /*请求数据*/

  //添加记录
  case class AddRecordReq(content: String)

  //跳转评论列表
  case class GoToCommentReq(id: Int)


  case class DelRecordReq(id: Int)


  /*返回结果数据*/
  //获得列表
  case class TaskRecord(
    id: Int,
    content: String,
    time: Long,
  )

  case class UserInfo(
                     id: Int,
                     userName: String
                     )

  case class CommentInfo(
                         id: Int,
                         content: String,
                         userInfo: UserInfo,
                         time: Long,
                       )

  case class GetListRsp(
    list: Option[List[TaskRecord]],
    errCode: Int = 0,
    msg: String = "Ok"
  ) extends CommonRsp




}
