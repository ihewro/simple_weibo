package com.neo.sk.todos2018.shared.ptcl

/**
  * User: sky
  * Date: 2018/6/1
  * Time: 15:45
  *
  * update by zhangtao: 2019-3-23
  * 前端给后端传递信息的结构体 req
 * 后端给前端传递信息的结构体 rsp
  */
object ToDoListProtocol {

  /*请求数据*/

  //添加记录
  case class AddRecordReq(content: String)

  case class AddCommentReq(
                          content: String,
                          recordId:Int
                          )

  //跳转评论列表
  case class GoToCommentReq(id: Int)


  case class FocusUserInfo (
                             userInfo: UserInfo,
                             isFocus: Boolean
  )

  case class DelRecordReq(id: Int)


  /*返回结果数据*/
  //获得列表
  case class TaskRecord(
    id: Int,
    content: String,
    time: Long,
    userInfo: UserInfo
  )

  case class UserInfo(
                     id: Int,
                     userName: String,
                     avatar: AvatarInfo
                     )

  case class PostUserReq(
                          password:String,
                          avatarId :Int
                        )

  case class AvatarInfo(
                   id: Int,
                   url: String
                   )
  case class CommentInfo(
                         id: Int,
                         content: String,
                         time: Long,
                         userInfo: UserInfo
                       )

  case class GetTaskRep(
                       info: TaskRecord,
                       errCode: Int = 0,
                       msg: String = "Ok"
                       )extends CommonRsp
  case class GetListRsp(
    list: Option[List[TaskRecord]],
    loginName: String,//加上一个当前登录用户的用户名，方便前端进行判断
    errCode: Int = 0,
    msg: String = "Ok"
  ) extends CommonRsp



  case class GetAvatarListRsp(
                             list: Option[List[AvatarInfo]],
                             loginName: String,
                             currentUseAvatarId: Int, //当前登录用户使用的头像id
                             errCode: Int = 0,
                             msg: String = "Ok"
                             )extends CommonRsp

  case class GetLikedUserListRsp(
                             list: Option[List[UserInfo]],
                             loginName: String,//加上一个当前登录用户的用户名，方便前端进行判断
                                isLike: Boolean,//当前登录用户是否已经点赞了
                             errCode: Int = 0,
                             msg: String = "Ok"
                           )extends CommonRsp

  case class GetUserListRsp(
                             list: Option[List[UserInfo]],
                             loginName: String,//加上一个当前登录用户的用户名，方便前端进行判断
                             errCode: Int = 0,
                             msg: String = "Ok"
                           )extends CommonRsp


  case class GetOtherUserRsp(
                            userInfo: UserInfo,
                            loginName: String,//加上一个当前登录用户的用户名，方便前端进行判断
                            isFocus: Boolean, //当前登录用户是否已经关注了该用户
                            errCode: Int = 0,
                            msg: String = "Ok"
                            )extends CommonRsp
  //返回评论列表的消息结构体
  case class GetCommentListRsq(
                                list: Option[List[CommentInfo]],
                                errCode: Int = 0,
                                msg: String = "Ok"
                              )extends CommonRsp

  case class GetRecommendUserListRsp(
                                      list: Option[List[FocusUserInfo]],
                                      errCode: Int = 0,
                                      msg: String = "Ok"
                                    )extends CommonRsp

}
