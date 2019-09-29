package com.neo.sk.todos2018.service

import akka.actor.Scheduler
import akka.http.scaladsl.server.Directives.{pathPrefix, _}
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.neo.sk.todos2018.Boot.executor
import com.neo.sk.todos2018.models.dao.UserDao
import com.neo.sk.todos2018.ptcl.Protocols.{notExitsError, parseError}
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol._
import com.neo.sk.todos2018.shared.ptcl.{ErrorRsp, SuccessRsp}
import org.slf4j.LoggerFactory

import scala.language.postfixOps

trait UserService extends ServiceUtils with SessionBase {

  import io.circe._
  import io.circe.generic.auto._

  implicit val timeout: Timeout

  implicit val scheduler: Scheduler

  private val log = LoggerFactory.getLogger(getClass)

  private val addOrCancelFocus = (path("addOrCancelFocus") & post){
    userAuth{
      user => entity(as[Either[Error,GoToCommentReq]]){
        case Left(error: Error) =>
          complete(parseError)
        case Right(req: GoToCommentReq) =>
          dealFutureResult(
            UserDao.addOrCancelFocus(req.id,user.userid).map{
              t=>
                if(t>0){
                  complete(SuccessRsp(0,"操作成功"))
                }else{
                  complete(ErrorRsp(1000101, "点赞操作失败"))
                }
            }
          )
      }
    }
  }

  private val getCurrentUser = (path("getCurrentUser") & post){
    userAuth{
      user =>
        entity(as [Either[Error,GoToCommentReq]]){
          case Left(error) =>
            complete(parseError)
          case Right(req) =>
            dealFutureResult(
              UserDao.getUserById(req.id,user.userid).map{
                qUser =>
                  if (qUser.isEmpty){
                    complete(notExitsError)
                  }else{
                    dealFutureResult(
                      UserDao.isFocus(req.id,user.userid).map{
                        re =>
                          var flag = false
                          if (re.isEmpty){
                            flag = false
                          }else{
                            flag = true
                          }
                          complete(GetOtherUserRsp(UserInfo(qUser.get._1.id,qUser.get._1.name,AvatarInfo(qUser.get._2.id,qUser.get._2.url)),user.userName,flag))
                      }
                    )
                  }
              }
            )
        }
    }
  }

  private val getAvatarList = (path("getAvatarList") & get){
    userAuth{
      user => {
        dealFutureResult2(
          UserDao.getAvatarList.map{
            list =>
              val data = list.map{
              l =>
                AvatarInfo(l.id,l.url)
              }.toList
              UserDao.getAvatarId(user.userid).map(
                id =>
                  complete(GetAvatarListRsp(Some(data),user.userName,id))
              )
          }
        )
      }
    }
  }

  private val editProfile = (path("editProfile") & post){
    userAuth{
      user => entity(as[Either[Error,PostUserReq]]){
        case Left(error: Error) =>
          complete(parseError)
        case Right(req) =>
          dealFutureResult(
            UserDao.editProfile(user.userid,req.avatarId,req.password).map(
              r =>
                if (r > 0){
                  complete(SuccessRsp())
                }else{
                  complete(ErrorRsp(1000101, "修改数据"))
                }
            )
          )
      }
    }
  }

  private val getLikedUserListByRecordId = (path("getLikedUserListByRecordId") & post){
    userAuth{
      user => entity(as[Either[Error,GoToCommentReq]]){
        case Left(error: Error) =>
          complete(parseError)
        case Right(req) =>
          dealFutureResult2(
            UserDao.getLikedUserListByRecordId(req.id).map{
              list =>
                val data = list.map{
                  l =>
                    UserInfo(l._1.id,l._1.name,AvatarInfo(l._2.id,l._2.url))
                }.toList

                UserDao.isLike(user.userid,req.id).map{
                  l =>
                    var flag = false
                    if (l.isEmpty){
                      flag = false
                    }else{
                      flag = true
                    }
                    complete(GetLikedUserListRsp(Some(data),user.userName,flag))
                }
            }
          )
      }
    }
  }


  private val addOrCancelLike = (path("addOrCancelLike") & post){
    userAuth{
      user => entity(as[Either[Error,GoToCommentReq]]){
        case Left(error: Error) =>
          complete(parseError)
        case Right(req: GoToCommentReq) =>
          dealFutureResult(
            UserDao.addOrCancelLike(user.userid,req.id).map{
              t=>
                if(t>0){
                  complete(SuccessRsp())
                }else{
                  complete(ErrorRsp(1000101, "关注操作失败"))
                }
            }
          )
      }
    }
  }


  private val getConcernList = (path("getConcernList") & post){
    userAuth{
      user => entity(as[Either[Error,GoToCommentReq]]){
        case Left(error: Error) =>
          complete(parseError)
        case Right(req: GoToCommentReq) =>
          dealFutureResult(
            UserDao.getConcernList(req.id).map{
              list =>
               val data = list.map{
                 l=>
                   UserInfo(l._1.id,l._1.name,AvatarInfo(l._2.id,l._2.url))
               }.toList
                complete(GetUserListRsp(Some(data),user.userName))
            }
          )
      }
    }
  }


  private val getFansList = (path("getFansList") & post){
    userAuth{
      user => entity(as[Either[Error,GoToCommentReq]]){
        case Left(error: Error) =>
          complete(parseError)
        case Right(req: GoToCommentReq) =>
          dealFutureResult(
            UserDao.getFansList(req.id).map{
              list =>
                val data = list.map{
                  l=>
                    UserInfo(l._1.id,l._1.name,AvatarInfo(l._2.id,l._2.url))
                }.toList
                complete(GetUserListRsp(Some(data),user.userName))
            }
          )
      }
    }
  }


  private val getRecommendUserList = (path("getRecommendUserList") & get){
    userAuth{
      user =>
        dealFutureResult2{
          UserDao.getRecommendUserList(user.userid).map{
            list =>
              val data = list.map{
                l=>
                  var flag = false
                  UserDao.isFocus(l._1.id,user.userid).map{
                    t =>
                      if (t.isEmpty){
                        flag = false
                      }else{
                        flag = true
                      }
                      FocusUserInfo(UserInfo(l._1.id,l._1.name,AvatarInfo(l._2.id,l._2.url)),flag)
                  }
              }.toList
              val rst = scala.concurrent.Future.sequence(data)
              rst.map{
                data =>
                  complete(GetRecommendUserListRsp(Some(data)))
              }
          }
        }
    }
  }

  val userRoutes: Route =
    pathPrefix("user") {
      addOrCancelFocus ~ getCurrentUser ~ getAvatarList ~ editProfile ~ getLikedUserListByRecordId ~ addOrCancelLike ~ getConcernList ~ getFansList ~ getRecommendUserList
    }
}
