package com.neo.sk.todos2018.models.dao

import com.neo.sk.todos2018.Boot.executor
import com.neo.sk.todos2018.models.SlickTables._
import com.neo.sk.todos2018.utils.DBUtil.db
import org.slf4j.LoggerFactory
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

/**
 * 操作用户数据库的相关方法
 */
object UserDao {
  private val log = LoggerFactory.getLogger(this.getClass)


  def getRecommendUserList(userId: Int) :Future[Seq[(rUserInfo,rAvatar)]] = {
    try{
      val q = for{
        user <- tUserInfo if user.id =!= userId //过滤到当前登录的用户
        avatar <- tAvatar if avatar.id === user.avatarId
      }yield (user,avatar)
      db.run(q.result)
    }catch {
      case e:Throwable =>
        log.error(s"get recordList error with error $e")
        Future.successful(Nil)
    }
  }

  def getConcernList(userId:Int):Future[Seq[(rUserInfo,rAvatar)]] = {
    val q = for{
      t <- tUserRelationship if t.xiaodiId ===userId
      user <- tUserInfo if  t.dageId === user.id
      avatar <- tAvatar if avatar.id === user.avatarId
    }yield (user,avatar)
    db.run(q.result)
  }



  def getFansList(userId:Int):Future[Seq[(rUserInfo,rAvatar)]] = {
    val q = for{
      t <- tUserRelationship if t.dageId ===userId
      user <- tUserInfo if  t.xiaodiId === user.id
      avatar <- tAvatar if avatar.id === user.avatarId
    }yield (user,avatar)
    db.run(q.result)
  }

  def editProfile(userId:Int, avatarId:Int,password:String): Future[Int] ={
    try {
      if (password !=""){
        val query = for{
          t <- tUserInfo if t.id === userId
        }yield (t.password,t.avatarId)
        db.run(query.update(password,avatarId))
      }else{
        val query = for{
          t <- tUserInfo if t.id === userId
        }yield (t.avatarId)
        val action = query.update(avatarId)
        db.run(action)
      }
    }catch {
      case e:Throwable =>
        log.error(s"get recordList error with error $e")
        Future.successful(-1)
    }
  }

  def getAvatarList():Future[Seq[rAvatar]] = {
    try {
      db.run(tAvatar.result)
    }catch {
      case e:Throwable =>
        log.error(s"get recordList error with error $e")
        Future.successful(Nil)
    }
  }

  def getAvatarId(userId : Int):Future[Int] = {
    db.run(tUserInfo.filter(t=> t.id === userId).map(_.avatarId).result.head)
  }


  def addOrCancelLike(userId: Int,recordId:Int):Future[Int] = {
    try{
      //先查询是否存在关联，如果已经关注了，则取消关注，否则关注
      for{
        r <- db.run{tRecordLikeRelationship.filter(t=> t.recordId === recordId).filter(t=> t.userId === userId).result.headOption}
      }yield  if (r.isEmpty){//如果没有关联则增加关联
        db.run{tRecordLikeRelationship.map(t=> (t.recordId,t.userId)) += (recordId,userId)}
      }else{//取消关联
        db.run{tRecordLikeRelationship.filter(t=> t.recordId === recordId).filter(t=> t.userId === userId).delete}
      }

      Future.successful(1)

    }catch {
      case e:Throwable =>
        log.error(s"get recordList error with error $e")
        Future.successful(-1)
    }
  }

  def addOrCancelFocus(dageId: Int,xiaodiId:Int) : Future[Int] = {
    try{
      //先查询是否存在关联，如果已经关注了，则取消关注，否则关注
      for{
        r <- db.run{tUserRelationship.filter(t=> t.dageId === dageId).filter(t=> t.xiaodiId === xiaodiId).result.headOption}
      }yield  if (r.isEmpty){//如果没有关联则增加关联
        db.run{tUserRelationship.map(t=> (t.dageId,t.xiaodiId,t.time)) += (dageId,xiaodiId,System.currentTimeMillis())}
        println("添加关注成功")
      }else{//取消关联
        db.run{tUserRelationship.filter(t=> t.dageId === dageId).filter(t=> t.xiaodiId === xiaodiId).delete}
      }

      Future.successful(1)

    }catch {
      case e:Throwable =>
        log.error(s"get recordList error with error $e")
        Future.successful(-1)
    }
  }

  def getUserById(userId:Int,xiaodiId:Int) : Future[Option[(rUserInfo,rAvatar)]]={
    try {
      val query = for{
        user <- tUserInfo.filter(t=>t.password === "1")
        avatar <- tAvatar.filter(r=> r.id === user.avatarId)
      }yield (user,avatar)
      db.run{
        query.result.headOption
      }
    }catch {
      case e:Throwable =>
        log.error(s"get recordList error with error $e")
        Future.successful(None)
    }
  }

  //是否关注了对方
  def isFocus(dageId:Int,xiaodiId:Int) :Future[Option[rUserRelationship]] ={
    db.run{
      tUserRelationship.filter(t=> t.dageId === dageId).filter(t=> t.xiaodiId === xiaodiId).result.headOption
    }
  }


  def isLike(userId:Int,recordId:Int):Future[Option[rRecordLikeRelationship]]= {
    db.run{
      tRecordLikeRelationship.filter(t=> t.recordId === recordId).filter(t=> t.userId === userId).result.headOption
    }
  }


  def getLikedUserListByRecordId(recordId:Int): Future[Seq[(rUserInfo,rAvatar)]] ={
    val query = for{
      f <- tRecordLikeRelationship if f.recordId === recordId
      user <- tUserInfo if user.id === f.userId
      avatar <- tAvatar if avatar.id === user.avatarId
    }yield (user,avatar)
    db.run(query.result)
  }

}
