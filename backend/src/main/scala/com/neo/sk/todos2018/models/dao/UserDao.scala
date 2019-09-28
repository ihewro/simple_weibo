package com.neo.sk.todos2018.models.dao

import com.neo.sk.todos2018.models.SlickTables._
import com.neo.sk.todos2018.utils.DBUtil.db
import org.slf4j.LoggerFactory
import slick.jdbc.PostgresProfile.api._
import com.neo.sk.todos2018.models.SlickTables._
import com.neo.sk.todos2018.utils.DBUtil.db
import org.slf4j.LoggerFactory
import slick.jdbc.PostgresProfile.api._
import com.neo.sk.todos2018.Boot.executor
import scala.concurrent.Future

/**
 * 操作用户数据库的相关方法
 */
object UserDao {
  private val log = LoggerFactory.getLogger(this.getClass)

  def addOrCancelFocus(dageId: Int,xiaodiId:Int) : Future[Int] = {
    try{
      //先查询是否存在关联，如果已经关注了，则取消关注，否则关注
      for{
        r <- db.run{tUserRelationship.filter(t=> t.dageId === dageId).filter(t=> t.xiaodiId === xiaodiId).result.headOption}
      }yield  if (r.isEmpty){//如果没有关联则增加关联
        db.run{tUserRelationship.map(t=> (t.dageId,t.xiaodiId)) += (dageId,xiaodiId)}
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


}
