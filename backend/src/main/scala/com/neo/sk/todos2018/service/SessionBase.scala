package com.neo.sk.todos2018.service

import akka.http.scaladsl.server
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.BasicDirectives
import akka.http.scaladsl.server.{Directive, Directive1, RequestContext}
import com.neo.sk.todos2018.common.AppSettings
import com.neo.sk.todos2018.ptcl.UserProtocol.UserBaseInfo
import com.neo.sk.todos2018.shared.ptcl.ErrorRsp
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol.GoToCommentReq
import com.neo.sk.todos2018.utils.SessionSupport
import io.circe.generic.auto._
import org.slf4j.LoggerFactory
/**
  * User: Taoz
  * Date: 12/4/2016
  * Time: 7:57 PM
  * 创建session的结构体，以及对session的操作
  */

object SessionBase{
  private val log = LoggerFactory.getLogger(this.getClass)

  val SessionTypeKey = "STKey"
  val userSessionKey: String = "userSessionKey"
  val commentSessionKey: String = "commentSessionKey"


    object SessionKeys {
      val sessionType = "todos2018_session"
      val userType = "todos2018_userType"
      val userId = "todos2018_userId"
      val name = "todos2018_name"
      val commentId = "todos2018_commentId"
      val headImg = "head_img"
      val timestamp = "todos2018_timestamp"
    }


  //构造跳转评论存储的session结构体
    case class goToCommentSession(
                                 goToCommentReq: GoToCommentReq,
                                 time: Long
                                  ){
      def toSessionMap: Map[String, String] = {
        Map(
          SessionTypeKey -> SessionKeys.sessionType,
          SessionKeys.commentId -> goToCommentReq.id.toString,
          SessionKeys.timestamp -> time.toString
        )
      }
    }

  //构造登录时候存储的session结构体
    case class ToDoListSession(
                                userInfo:UserBaseInfo,
                                time: Long
                           ) {
      def toSessionMap: Map[String, String] = {
        Map(
          SessionTypeKey -> SessionKeys.sessionType,
          SessionKeys.name -> userInfo.userName,
          SessionKeys.userId -> userInfo.userid.toString,
          SessionKeys.timestamp -> time.toString
        )
      }
    }
}

trait SessionBase extends SessionSupport with ServiceUtils{

  import SessionBase._

  override val sessionEncoder = SessionSupport.PlaySessionEncoder
  override val sessionConfig = AppSettings.sessionConfig
  private val timeout = AppSettings.sessionTimeOut * 60 * 60 * 1000
  implicit class SessionTransformer(sessionMap: Map[String, String]) {
    def toToDoListSession:Option[ToDoListSession] = {
      try {
        if (sessionMap.get(SessionTypeKey).exists(_.equals(SessionKeys.sessionType))) {
          if(sessionMap(SessionKeys.timestamp).toLong - System.currentTimeMillis() > timeout){
            None
          }else {
            //匹配session
            Some(ToDoListSession(
                UserBaseInfo(sessionMap(SessionKeys.userId).toInt,sessionMap(SessionKeys.name)),
              sessionMap(SessionKeys.timestamp).toLong
            ))
          }
        } else {
          log.debug("no session type in the session")
          None
        }
      } catch {
        case e: Exception =>
          e.printStackTrace()
          log.warn(s"toAdminSession: ${e.getMessage}")
          None
      }
    }
  }
  def loggingAction: Directive[Tuple1[RequestContext]] = extractRequestContext.map { ctx =>
//    log.info(s"Access uri: ${ctx.request.uri} from ip ${ctx.request.uri.authority.host.address}.")
    ctx
  }
  protected val optionalToDoSession: Directive1[Option[ToDoListSession]] = optionalSession.flatMap {
    case Right(sessionMap) => BasicDirectives.provide(sessionMap.toToDoListSession)
    case Left(error) =>
      log.debug(error)
      BasicDirectives.provide(None)
  }

  def noSessionError(message:String = "no session") = ErrorRsp(1000102,s"$message")

  //用户认证
  def userAuth(f: UserBaseInfo => server.Route) = loggingAction { ctx =>
    optionalToDoSession {
      case Some(session) =>
        f(session.userInfo)
      case None =>
        complete(noSessionError())
    }
  }
}
