package com.neo.sk.todos2018.service

import akka.actor.Scheduler
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.neo.sk.todos2018.Boot.executor
import com.neo.sk.todos2018.models.dao.{LoginDAO, ToDoListDAO}
import com.neo.sk.todos2018.ptcl.Protocols.parseError
import com.neo.sk.todos2018.service.SessionBase.goToCommentSession
import com.neo.sk.todos2018.shared.ptcl.ToDoListProtocol._
import com.neo.sk.todos2018.shared.ptcl.{ErrorRsp, SuccessRsp}
import org.slf4j.LoggerFactory

import scala.language.postfixOps

/**
  * User: sky
  * Date: 2018/6/1
  * Time: 15:41
  * 2019/3/21 delete session check
  */
trait ToDoListService extends ServiceUtils with SessionBase {

  import io.circe._
  import io.circe.generic.auto._

  implicit val timeout: Timeout

  implicit val scheduler: Scheduler

  private val log = LoggerFactory.getLogger(getClass)

  private val addRecord = (path("addRecord") & post) {
    userAuth{ user =>
      entity(as[Either[Error, AddRecordReq]]) {
        case Left(error) =>
          log.warn(s"some error: $error")
          complete(parseError)
        case Right(req) =>
          dealFutureResult {
            val author = user.userid
            println(s"add record: ${req.content}")
            ToDoListDAO.addRecord(author, req.content).map { r =>
              if (r > 0) {
                complete(SuccessRsp())
              } else {
                complete(ErrorRsp(1000101, "add record error"))
              }
            }
          }
      }
    }
  }


  private val addComment = (path("addComment") & post){
    userAuth({
      user =>
        entity(as[Either[Error,AddCommentReq]]){
          case Left(error) =>
            complete(parseError)
          case Right(req) =>
            dealFutureResult(
              ToDoListDAO.addComment(req.recordId,user.userid,req.content).map{ r =>
                if (r>0){
                  complete(SuccessRsp())
                }else{
                  complete(ErrorRsp(1000101,"添加失败"))
                }
              }
            )
        }
    })
  }

  private val delRecord = (path("delRecord") & post) {
    userAuth{ user =>
      entity(as[Either[Error, DelRecordReq]]) {
        case Left(error) =>
          log.warn(s"some error: $error")
          complete(parseError)
        case Right(req) =>
          dealFutureResult {
            val id = req.id
            println(s"delete record: $id")
            ToDoListDAO.delRecord(id).map { r =>
              if (r > 0) {
                complete(SuccessRsp())
              } else {
                complete(ErrorRsp(1000101, "删除失败"))
              }
            }
          }
      }
    }
  }



  private val getRecordListByUser = (path("getRecordListByUser") & post) {
    userAuth{ user =>
      entity(as[Either[Error,GoToCommentReq]]){
        case Left(error) =>
          log.warn(s"some error: $error")
          complete(parseError)
        case Right(req) =>
          dealFutureResult2 {
            ToDoListDAO.getRecordListByUser(req.id).map { list =>
              val data = list.map { r=>
                //查询该username对象的user信息
                val users = for {
                  user <- LoginDAO.getUser(r.userid)
                  avatar <- LoginDAO.getUserAvatar((user.get.avatarId))
                } yield (user, avatar)

                users.map {
                  users2 =>
                    val user2 = users2._1.get
                    val avatar = users2._2.get
                    TaskRecord(r.id, r.content, r.time, UserInfo(user2.id, user2.name, AvatarInfo(avatar.id, avatar.url)))
                }
              }
              val rst = scala.concurrent.Future.sequence(data)
              rst.map{
                data =>
                  complete(GetListRsp(Some(data.toList),user.userName))
              }
            }
          }
      }
    }
  }




  private val getRecordListByLoginUser = (path("getRecordListByLoginUser") & get) {
    userAuth{ user =>
      dealFutureResult2 {
        println("登录用户" + user)
        ToDoListDAO.getRecordListByUser(user.userid).map { list =>
          val data = list.map { r=>
            //查询该username对象的user信息
            val users = for {
              user <- LoginDAO.getUser(r.userid)
              avatar <- LoginDAO.getUserAvatar((user.get.avatarId))
            } yield (user, avatar)

            users.map {
              users2 =>
                val user2 = users2._1.get
                val avatar = users2._2.get
                TaskRecord(r.id, r.content, r.time, UserInfo(user2.id, user2.name, AvatarInfo(avatar.id, avatar.url)))
            }
          }
          val rst = scala.concurrent.Future.sequence(data)
          rst.map{
            data =>
              complete(GetListRsp(Some(data.toList),user.userName))
          }
        }
      }
    }
  }


  private val getRecordById = (path("getRecordById") & post){
    userAuth{ user =>
      entity(as[Either[Error,GoToCommentReq]]){
        case Left(error) =>
          log.warn(s"some error: $error")
          complete(parseError)

        case Right(req) =>
          dealFutureResult2(
            ToDoListDAO.getRecordById(req.id).map { list =>
              val data = list.map { r=>
                //查询该username对象的user信息
                val users = for {
                  user <- LoginDAO.getUser(r.userid)
                  avatar <- LoginDAO.getUserAvatar((user.get.avatarId))
                } yield (user, avatar)

                users.map {
                  users2 =>
                    val user2 = users2._1.get
                    val avatar = users2._2.get
                    TaskRecord(r.id, r.content, r.time, UserInfo(user2.id, user2.name, AvatarInfo(avatar.id, avatar.url)))
                }
              }
              val rst = scala.concurrent.Future.sequence(data)
              rst.map{
                data =>
                  complete(GetListRsp(Some(data.toList),user.userName))
              }
            }
          )
      }
    }
  }

  private val goComment = (path("goComment") & post){
    userAuth{ user =>
      println("开始跳转评论")
      entity(as[Either[Error,GoToCommentReq]]){
        case Left(error) =>
          println(GoToCommentReq)
          println("跳转评论2")
          complete(parseError)
        case Right(req) =>
          println("跳转评论")
          //把当前点击的评论id存到session中
          val session = goToCommentSession(GoToCommentReq(req.id), System.currentTimeMillis())
          addSession(session.toSessionMap){
            println("跳转评论2333")
            complete(SuccessRsp())
          }
      }
    }
  }

  private val getCommentListByRecordId = (path("getCommentListByRecordId") & post){
    userAuth{
      user =>
        println("获取评论列表")
        entity(as[Either[Error,GoToCommentReq]]){
          case Left(error) =>
            complete(parseError)
          case Right(req) =>
            dealFutureResult2(
              ToDoListDAO.getCommentListByRecordId(req.id).map{
                list =>
                  val data = list.map{
                    comment =>

                      val users = for {
                        user <- LoginDAO.getUserById(comment.userid.get)
                        avatar <- LoginDAO.getUserAvatar((user.get.avatarId))
                      } yield (user, avatar)

                      users.map {
                        users2 =>
                          val user2 = users2._1.get
                          val avatar = users2._2.get
                          CommentInfo(comment.id, comment.content.get, comment.time.get, UserInfo(user2.id, user2.name, AvatarInfo(avatar.id, avatar.url)))
                      }

                  }
                  val rst = scala.concurrent.Future.sequence(data)
                  rst.map{
                    data =>
                      complete(GetCommentListRsq(Some(data.toList)))
                  }
              }
            )
        }
    }
  }

  private val getFocusRecordList = (path("getFocusRecordList") & get){
    userAuth{
      user =>
        dealFutureResult2{
          println("登录用户id" + user.userid)
          ToDoListDAO.getFocusRecordByUser(user.userid).map{ list=>

            println("关注的人微博数目"+list.length)
            val data = list.map { r=>

              //查询该username对象的user信息
              val users = for {
                user <- LoginDAO.getUser(r.userid)
                avatar <- LoginDAO.getUserAvatar((user.get.avatarId))
              } yield (user, avatar)

              users.map {
                users2 =>
                  val user2 = users2._1.get
                  val avatar = users2._2.get
                  TaskRecord(r.id, r.content, r.time, UserInfo(user2.id, user2.name, AvatarInfo(avatar.id, avatar.url)))
              }
            }
            val rst = scala.concurrent.Future.sequence(data)
            rst.map{
              data =>
                complete(GetListRsp(Some(data.toList),user.userName))
            }
          }
      }
    }
  }


  private val getRecentHotList  = (path("getRecentHotList") & get){
    userAuth{
      user =>
        dealFutureResult2(
          ToDoListDAO.getRecentHotList.map{list=>
            val data = list.map { r=>
              //查询该username对象的user信息
              val users = for {
                user <- LoginDAO.getUser(r.userid)
                avatar <- LoginDAO.getUserAvatar((user.get.avatarId))
              } yield (user, avatar)

              users.map {
                users2 =>
                  val user2 = users2._1.get
                  val avatar = users2._2.get
                  TaskRecord(r.id, r.content, r.time, UserInfo(user2.id, user2.name, AvatarInfo(avatar.id, avatar.url)))
              }
            }
            val rst = scala.concurrent.Future.sequence(data)
            rst.map{
              data =>
                complete(GetListRsp(Some(data.toList),user.userName))
            }
          }
        )
    }
  }

  val listRoutes: Route =
    pathPrefix("list") {
      addRecord ~ delRecord ~ getRecordListByLoginUser ~ goComment ~ getRecordById ~ getCommentListByRecordId ~ addComment ~ getFocusRecordList ~ getRecentHotList ~ getRecordListByUser
    }
}
