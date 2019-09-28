package com.neo.sk.todos2018.service

import akka.actor.Scheduler
import akka.http.scaladsl.server.Directives.{pathPrefix, _}
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.neo.sk.todos2018.Boot.executor
import com.neo.sk.todos2018.models.dao.UserDao
import com.neo.sk.todos2018.ptcl.Protocols.parseError
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol._
import com.neo.sk.todos2018.shared.ptcl.{ErrorRsp, SuccessRsp}
import org.slf4j.LoggerFactory

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


  val listRoutes: Route =
    pathPrefix("user") {
      addOrCancelFocus
    }
}
