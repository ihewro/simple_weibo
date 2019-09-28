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
                  complete(SuccessRsp())
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
                      UserDao.isFocus(user.userid,req.id).map{
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
                  complete(GetAvatarListRsp(Some(data),id))
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


  val userRoutes: Route =
    pathPrefix("user") {
      addOrCancelFocus ~ getCurrentUser ~ getAvatarList ~ editProfile
    }
}
